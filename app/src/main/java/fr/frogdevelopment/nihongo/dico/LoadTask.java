package fr.frogdevelopment.nihongo.dico;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
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
		List<Entry> entries;

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

			try (BufferedInputStream is = new BufferedInputStream(connection.getInputStream())) {
				publishProgress("reading value");
				ObjectMapper mapper = new ObjectMapper();
				entries = mapper.readValue(is, new TypeReference<List<Entry>>() {
				});
			}

		} catch (IOException e) {
			e.printStackTrace(); // fixme
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		NumberFormat percentInstance = NumberFormat.getPercentInstance();

		int index = 0;
		double percent = 0.0;
		int total = entries.size();
		ContentValues[] bulkToInsert;
		ContentValues entryValues;
		ContentValues senseValues;
		List<ContentValues> mValueList = new ArrayList<>();
		for (Entry entry : entries) {
			index++;
			double percentTmp = ((double) index) / total;
			if (percentTmp > percent) {
				publishProgress("fetching entries " + percentInstance.format(percentTmp));
				percent = percentTmp;
			}
			entryValues = new ContentValues();
			entryValues.put("tag", "entry");
			entryValues.put("key", index);
			entryValues.put(EntryContract.KANJI, entry.kanji);
			entryValues.put(EntryContract.READING, entry.reading);

			mValueList.add(entryValues);

			for (Sense sense : entry.senses) {
				senseValues = new ContentValues();
				senseValues.put("tag", "sense");
				senseValues.put("key", index);
				senseValues.put(SenseContract.POS, StringUtils.join(sense.pos, ", "));
				senseValues.put(SenseContract.FIELD, StringUtils.join(sense.field, ", "));
				senseValues.put(SenseContract.MISC, StringUtils.join(sense.misc, ", "));
				senseValues.put(SenseContract.DIAL, StringUtils.join(sense.dial, ", "));
				senseValues.put(SenseContract.GLOSS, StringUtils.join(sense.gloss, ", "));

				mValueList.add(senseValues);
			}

			if (mValueList.size() > 4000) { // todo find best limit before insert loop
				bulkToInsert = mValueList.toArray(new ContentValues[mValueList.size()]);
				context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_WORD, bulkToInsert); // fixme delete data inserted if error

				mValueList.clear();
			}
		}

		return true;
	}

	@Override
	protected void onProgressUpdate(String... text) {
		progressDialog.setMessage(text[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mWakeLock.release();
		progressDialog.dismiss();

		if (result) {
			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
			edit.putBoolean("data_saved", true);
			edit.apply();
		}
	}
}
