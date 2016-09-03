package fr.frogdevelopment.nihongo.dico;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Entry;
import fr.frogdevelopment.nihongo.dico.entities.Sense;

class LoadTask extends AsyncTask<Void, String, Boolean> {

	private static final String BASE_URL = "http://legall.benoit.free.fr/nihon_go/";

	private       PowerManager.WakeLock mWakeLock;
	private       ProgressDialog        progressDialog;
	private final Context               context;

	public LoadTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		mWakeLock.acquire();
		progressDialog = ProgressDialog.show(context, "Download data", "Fetching data");
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {
		};
		HttpURLConnection connection = null;
		try {
			URL url = new URL(BASE_URL + "entries_eng.json"); // fixme select language
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				publishProgress("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
				return false;
			}

			// this will be useful to display download percentage
			// might be -1: server did not report the length
			int fileLength = connection.getContentLength();

			NumberFormat percentInstance = NumberFormat.getPercentInstance();

			try (InputStream is = connection.getInputStream()) {
				publishProgress("récupération fichier"); // fixme publish progression
				List<Entry> entries = mapper.readValue(is, mapType);

				int nbEntries = entries.size();
				publishProgress("parsing fichier : " + nbEntries + " entries");
				ContentValues[] bulkToInsert;
				List<ContentValues> mValueList = new ArrayList<>();
				int key = 0;
				for (Entry entry : entries) {
					key++;
					String percent = percentInstance.format(key / nbEntries);
					publishProgress("parsing entry " + percent);
					ContentValues entryValues = new ContentValues();
					entryValues.put("tag", "entry");
					entryValues.put("key", key);
					entryValues.put(EntryContract.KANJI, entry.kanji);
					entryValues.put(EntryContract.READING, entry.reading);

					mValueList.add(entryValues);

					for (Sense sense : entry.senses) {
						ContentValues senseValues = new ContentValues();
						senseValues.put("tag", "sense");
						senseValues.put("key", key);
						senseValues.put(SenseContract.POS, StringUtils.join(sense.pos, ", "));
						senseValues.put(SenseContract.FIELD, StringUtils.join(sense.field, ", "));
						senseValues.put(SenseContract.MISC, StringUtils.join(sense.misc, ", "));
						senseValues.put(SenseContract.DIAL, StringUtils.join(sense.dial, ", "));
						senseValues.put(SenseContract.GLOSS, StringUtils.join(sense.gloss, ", "));

						mValueList.add(senseValues);
					}

					if (mValueList.size() > 1000) { // todo find limit before insert
//						publishProgress("sauvegarde données");
						bulkToInsert = new ContentValues[mValueList.size()];
						mValueList.toArray(bulkToInsert);
						context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_WORD, bulkToInsert);

						mValueList.clear();
					}
				}

				publishProgress("Fini");
			}

		} catch (IOException e) {
			e.printStackTrace(); // fixme delete data inserted if error
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return true;
	}

	@Override
	protected void onProgressUpdate(String... text) {
		progressDialog.setMessage(text[0]); // todo progress via % ???
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mWakeLock.release();
		progressDialog.dismiss();

//		if (result) {
//			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
//			edit.putBoolean("data_saved", true);
//			edit.apply();
//		}
	}
}
