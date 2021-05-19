package com.frogdevelopment.nihongo.dico.service;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.frogdevelopment.nihongo.dico.R;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.RandomUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

abstract class AbstractDownloadJobIntentService extends JobIntentService {

    private final int     notificationId;
    private final int     notificationTitle;
    private final Handler mHandler = new Handler();

    protected AbstractDownloadJobIntentService(int notificationTitle) {
        this.notificationId = RandomUtils.nextInt();
        this.notificationTitle = notificationTitle;
    }

    protected void download(final String fileName, final Uri uri, final Function<CSVRecord, ContentValues> toContentValues) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://legall.benoit.free.fr/nihongo/dico/" + fileName);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return;
            }

            loopOnLines(connection, uri, toContentValues);

        } catch (Exception e) {
            Log.e("AbstractDownloadJobIntentService", "Can not fetch data", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void loopOnLines(final HttpURLConnection connection, final Uri uri, final Function<CSVRecord, ContentValues> toContentValues) throws IOException {
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
                    contentValues.add(toContentValues.apply(record));
                    nbValues++;

                    // todo find best limit before insert loop
                    if (nbValues > 4000) {
                        getContentResolver().bulkInsert(uri, contentValues.toArray(new ContentValues[nbValues]));

                        nbValues = 0;
                        contentValues.clear();
                    }
                }

                if (!contentValues.isEmpty()) {
                    getContentResolver().bulkInsert(uri, contentValues.toArray(new ContentValues[0]));
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DICO_DOWNLOAD_CHANNEL");
        builder.setContentTitle(getString(notificationTitle))
               .setContentText(getString(R.string.downloads_notify_download_complete))
               .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
               .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               .setProgress(0, 0, false);
        notificationManager.notify(notificationId, builder.build());
    }

    protected void notify(final int progress, int resId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DICO_DOWNLOAD_CHANNEL");
        builder.setContentTitle(getString(notificationTitle))
               .setContentText(getString(resId))
               .setSmallIcon(R.drawable.ic_baseline_download_24)
               .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               .setOngoing(true)
               .setProgress(100, progress, false);

        notificationManager.notify(notificationId, builder.build());
    }

    protected void saveDate(String preferenceName) {
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
        Instant now = Instant.now();
        long toEpochMilli = now.toEpochMilli();
        editor.putLong(preferenceName, toEpochMilli);
        editor.apply();

        Intent intent = new Intent();
        intent.setAction("com.frogdevelopment.nihongo.dico.Downloaded");
        intent.putExtra("preferenceName", preferenceName);
        sendBroadcast(intent);
    }

    protected void toast(final int resId) {
        mHandler.post(() -> Toast.makeText(AbstractDownloadJobIntentService.this, resId, Toast.LENGTH_SHORT).show());
    }

}
