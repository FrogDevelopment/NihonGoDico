package com.frogdevelopment.nihongo.dico.ui.settings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

abstract class AbstractDownLoadTask extends AsyncTask<String, Integer, Boolean> {

    interface DownloadListener {
        void downloadDone();
    }

    private static final String BASE_URL = "http://legall.benoit.free.fr/nihongo/dico/";

    private static final int MAX_VALUES = 4000;

    private final WeakReference<Context> mContext;
    private final Uri mUri;
    private final DownloadListener mListener;

    protected AbstractDownLoadTask(Context context, Uri uri, DownloadListener listener) {
        this.mContext = new WeakReference<>(context);
        this.mUri = uri;
        this.mListener = listener;
    }

    private Context getContext() {
        return mContext.get();
    }

    @Override
    protected Boolean doInBackground(String... languages) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL + getFileName(languages[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // fixme
//				publishProgress("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
                return false;
            }

            loopOnLines(connection);

        } catch (IOException e) {
            Log.e("NGD", "Can not fetch data", e);
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return true;
    }

    protected abstract String getFileName(final String language);

    private void loopOnLines(HttpURLConnection connection) throws IOException {
        final ContentResolver contentResolver = getContext().getContentResolver();
        try (final BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
             final GzipCompressorInputStream inputStream = new GzipCompressorInputStream(is);
             final TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream)) {
            ArchiveEntry entry;
            while (null != (entry = tarIn.getNextEntry())) {
                if (entry.getSize() < 1) {
                    continue;
                }

                // parse lines
                final InputStreamReader reader = new InputStreamReader(tarIn, UTF_8);
                final CSVParser parse = CSVFormat.DEFAULT
                        .withHeader()
                        .withSkipHeaderRecord()
                        .parse(reader);

                int nbValues = 0;
                final List<ContentValues> contentValues = new ArrayList<>();
                for (CSVRecord record : parse.getRecords()) {
                    contentValues.add(toContentValues(record));
                    nbValues++;

                    // todo find best limit before insert loop
                    if (nbValues > MAX_VALUES) {
                        contentResolver.bulkInsert(mUri, contentValues.toArray(new ContentValues[nbValues]));

                        nbValues = 0;
                        contentValues.clear();
                    }

                    // allow canceling with back button
                    if (isCancelled()) {
                        break;
                    }

//                    publishProgress(++currentLine * 100 / nbLines);
                }

                if (!contentValues.isEmpty()) {
                    contentResolver.bulkInsert(mUri, contentValues.toArray(new ContentValues[0]));
                }
            }
        }

//        notifyRebuild(contentResolver);
    }

    abstract protected ContentValues toContentValues(final CSVRecord record);

//    abstract protected void notifyRebuild(final ContentResolver contentResolver);


    @Override
    protected void onPostExecute(Boolean result) {
            mListener.downloadDone();
    }
}
