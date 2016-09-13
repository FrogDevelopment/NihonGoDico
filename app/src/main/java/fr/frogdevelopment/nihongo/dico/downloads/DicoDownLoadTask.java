package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;

class DicoDownLoadTask extends AsyncTask<Void, String, Boolean> {

	private static final String BASE_URL = "http://legall.benoit.free.fr/nihon_go/";

	private       PowerManager.WakeLock mWakeLock;
	private       ProgressDialog        progressDialog;
	private final Context               context;

	public DicoDownLoadTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		mWakeLock.acquire();
		progressDialog = ProgressDialog.show(context, "Download entries", "Fetching entries");
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		List<ContentValues> mValueList = new ArrayList<>();

		HttpURLConnection connection = null;
		try {
			URL url = new URL(BASE_URL + "entries_fre.txt"); // fixme select language
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				publishProgress("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
				return false;
			}

			NumberFormat percentInstance = NumberFormat.getPercentInstance();
			try (BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
			     Scanner scanner = new Scanner(is)) {

				int index = 0;
				double percent = 0.0;
				int total = Integer.valueOf(scanner.nextLine());
				ContentValues[] bulkToInsert;
				ContentValues entryValues;
				ContentValues senseValues;

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();

					index++;
					double percentTmp = ((double) index) / total;
					if (percentTmp > percent) {
						publishProgress("fetching entries " + percentInstance.format(percentTmp));
						percent = percentTmp;
					}

					String[] values = line.split("\\|", 3);

					entryValues = new ContentValues();
					entryValues.put("tag", "entry");
					entryValues.put("key", index);
					entryValues.put(EntryContract.KANJI, values[0]);
					entryValues.put(EntryContract.READING, values[1]);

					mValueList.add(entryValues);

					String[] senses = values[2].split("\\|");

					for (String sense : senses) {
						String[] values2 = sense.split("/");
						senseValues = new ContentValues();
						senseValues.put("tag", "sense");
						senseValues.put("key", index);
						senseValues.put(SenseContract.POS, values2[0].replaceAll(";", ", "));
						senseValues.put(SenseContract.FIELD, values2[1].replaceAll(";", ", "));
						senseValues.put(SenseContract.MISC, values2[2].replaceAll(";", ", "));
						senseValues.put(SenseContract.INFO, values2[3]);
						senseValues.put(SenseContract.DIAL, values2[4].replaceAll(";", ", "));
						senseValues.put(SenseContract.GLOSS, values2[5].replaceAll(";", ", "));

						mValueList.add(senseValues);
					}

					if (mValueList.size() > 4000) { // todo find best limit before insert loop
						bulkToInsert = mValueList.toArray(new ContentValues[mValueList.size()]);
						context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_WORD, bulkToInsert); // fixme delete data inserted if error

						mValueList.clear();
					}

				}

				bulkToInsert = mValueList.toArray(new ContentValues[mValueList.size()]);
				context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_WORD, bulkToInsert); // fixme delete data inserted if error
			}

		} catch (IOException e) {
			e.printStackTrace(); // fixme
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
