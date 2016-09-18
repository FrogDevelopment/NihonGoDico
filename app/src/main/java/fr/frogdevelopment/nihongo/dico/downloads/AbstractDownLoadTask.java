package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class AbstractDownLoadTask extends AsyncTask<Void, Integer, Boolean> {

	private static final String BASE_URL = "http://legall.benoit.free.fr/nihon_go/";

	private static final int MAX_VALUES = 4000;

	protected final Context mContext;
	private final   int     mMessageResource;
	private final   String  mFileName;
	private final   Uri     mUri;
	private final   String  mTag;

	private PowerManager.WakeLock mWakeLock;
	private ProgressDialog        mProgressDialog;

	protected List<ContentValues> mValueList = new ArrayList<>();

	public AbstractDownLoadTask(Context context, int messageResource, String fileName, Uri uri, String tag) {
		this.mContext = context;
		this.mMessageResource = messageResource;
		this.mFileName = fileName;
		this.mUri = uri;
		this.mTag = tag;
	}

	@Override
	protected void onPreExecute() {
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		mWakeLock.acquire();
//		mProgressDialog = ProgressDialog.show(mContext, "Downloading", "Downloading");
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("Downloading"); //fixme
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setIndeterminate(true);
//		mProgressDialog.setCancelMessage() fixme
		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(android.R.string.cancel), (dialog, which) -> {
			cancel(true);
		});

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
		try (BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
		     Scanner scanner = new Scanner(is)) {

			publishProgress(mMessageResource);

			int nbValues = 0;
			while (scanner.hasNextLine()) {
				fillValues(scanner.nextLine());
				nbValues++;

				// todo find best limit before insert loop
				if (nbValues > MAX_VALUES) {
					mContext.getContentResolver().bulkInsert(mUri, mValueList.toArray(new ContentValues[nbValues]));

					nbValues = 0;
					mValueList.clear();
				}
			}

			if (!mValueList.isEmpty()) {
				mContext.getContentResolver().bulkInsert(mUri, mValueList.toArray(new ContentValues[mValueList.size()]));
			}
		}
	}

	abstract protected void fillValues(String currentLine);

	@Override
	protected void onProgressUpdate(Integer... text) {
		mProgressDialog.setMessage(mContext.getString(text[0]));
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mWakeLock.release();
		mProgressDialog.dismiss();

		if (result) {
			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			edit.putBoolean(mTag, true);
			edit.apply();
		}
	}
}
