package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class AbstractDownLoadTask extends AsyncTask<Void, Integer, Boolean> {

	interface DownloadListener {
		void downloadDone();
	}

	private static final String BASE_URL = "http://legall.benoit.free.fr/nihon_go/";

	private static final int MAX_VALUES = 4000;

	protected final Context          mContext;
	private final   String           mFileName;
	private final   Uri              mUri;
	private final   String           mTag;
	private final   DownloadListener mListener;

	private PowerManager.WakeLock mWakeLock;
	private ProgressDialog        mProgressDialog;

	protected List<ContentValues> mValueList = new ArrayList<>();

	protected AbstractDownLoadTask(Context context, String fileName, Uri uri, String tag, DownloadListener listener) {
		this.mContext = context;
		this.mFileName = fileName;
		this.mUri = uri;
		this.mTag = tag;
		this.mListener = listener;
	}

	@Override
	protected void onPreExecute() {
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		if (pm != null) {
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
			mWakeLock.acquire(10*60*1000L /*10 minutes*/);
		}
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Downloading"); //fixme
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(BASE_URL + mFileName);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				// fixme
//				publishProgress("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
				return false;
			}

			loopOnLines(connection);

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

	protected void loopOnLines(HttpURLConnection connection) throws IOException {
		int fileLength = connection.getContentLength();
		try (BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
		     LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(is));
		     Scanner scanner = new Scanner(is)) {

			is.mark(fileLength); //avoid IOException on reset

			int nbLines = 0;

			while (lineNumberReader.readLine() != null) {
				nbLines++;
			}

			is.reset();
			is.mark(fileLength);

			int nbValues = 0;
			int currentLine = 0;
			while (scanner.hasNextLine()) {
				fillValues(scanner.nextLine());
				nbValues++;

				// todo find best limit before insert loop
				if (nbValues > MAX_VALUES) {
					mContext.getContentResolver().bulkInsert(mUri, mValueList.toArray(new ContentValues[nbValues]));

					nbValues = 0;
					mValueList.clear();
				}

				// allow canceling with back button
				if (isCancelled()) {
					break;
				}
				currentLine++;

				publishProgress(currentLine * 100 / nbLines);
			}

			if (!mValueList.isEmpty()) {
				mContext.getContentResolver().bulkInsert(mUri, mValueList.toArray(new ContentValues[mValueList.size()]));
			}
		}
	}

	abstract protected void fillValues(String currentLine);

	@Override
	protected void onProgressUpdate(Integer... text) {
		mProgressDialog.setProgress(text[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mWakeLock.release();
		mProgressDialog.dismiss();

		if (result) {
			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			edit.putBoolean(mTag, true);
			edit.apply();

			mListener.downloadDone();
		}
	}
}
