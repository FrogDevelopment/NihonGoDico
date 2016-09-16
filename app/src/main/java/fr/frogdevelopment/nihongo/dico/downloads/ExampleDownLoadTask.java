package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.contentprovider.ExampleContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;

class ExampleDownLoadTask extends AsyncTask<Void, String, Boolean> {

    private static final String BASE_URL = "http://legall.benoit.free.fr/nihon_go/";

    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog progressDialog;
    private final Context context;

    public ExampleDownLoadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire();
        progressDialog = ProgressDialog.show(context, "Download examples", "Fetching examples");
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        List<ContentValues> mValueList = new ArrayList<>();

//		HttpURLConnection connection = null;
        String currentLine = "";
        try {
//			URL url = new URL(BASE_URL + "entries_fre.txt"); // fixme select language
//			connection = (HttpURLConnection) url.openConnection();
//			connection.connect();
//
//			// expect HTTP 200 OK, so we don't mistakenly save error report
//			// instead of the file
//			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//				publishProgress("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
//				return false;
//			}

//			try (BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
            try (InputStream is = context.getResources().openRawResource(R.raw.examples_jpn_fra);
                 Scanner scanner = new Scanner(is)) {

                ContentValues[] bulkToInsert;
                ContentValues contentValues;

                publishProgress("fetching examples");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    currentLine = line;

                    // line format : Jpn_seq_no[TAB]Eng_seq_no[TAB]Japanese sentence[TAB]English sentence[TAB]Indices
                    String[] values = line.split("\\t");
                    contentValues = new ContentValues();
                    contentValues.put(ExampleContract.JAPANESE_REF, values[0]);
                    contentValues.put(ExampleContract.TRANSLATION_REF, values[1]);
                    contentValues.put(ExampleContract.JAPANESE_SENTENCE, values[2]);
                    contentValues.put(ExampleContract.TRANSLATION_SENTENCE, values[3]);
                    if (values.length == 5) { // fixme pourquoi des indices sont absents ?
                        contentValues.put(ExampleContract.INDICES, values[4]);
                    } else {
                        contentValues.put(ExampleContract.INDICES, values[2]);
                    }

                    mValueList.add(contentValues);

                    if (mValueList.size() > 4000) { // todo find best limit before insert loop
                        bulkToInsert = mValueList.toArray(new ContentValues[mValueList.size()]);
                        context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_EXAMPLE, bulkToInsert);

                        mValueList.clear();
                    }
                }

                if (!mValueList.isEmpty()) {
                    bulkToInsert = mValueList.toArray(new ContentValues[mValueList.size()]);
                    context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_EXAMPLE, bulkToInsert);
                }

                context.getContentResolver().update(NihonGoDicoContentProvider.URI_REBUILD, null, null, null);
            }

        } catch (IOException e) {
            System.out.println(currentLine);
            e.printStackTrace(); // fixme
            return false;
//		} finally {
//			if (connection != null) {
//				connection.disconnect();
//			}
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

//		if (result) {
//			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
//			edit.putBoolean("data_saved", true);
//			edit.apply();
//		}
    }
}
