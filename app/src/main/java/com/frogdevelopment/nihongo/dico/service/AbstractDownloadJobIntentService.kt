package com.frogdevelopment.nihongo.dico.service

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.frogdevelopment.nihongo.dico.R
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.apache.commons.lang3.RandomUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*
import java.util.function.Function

internal abstract class AbstractDownloadJobIntentService protected constructor(private val notificationTitle: Int) :

    JobIntentService() {
    private val notificationId: Int = RandomUtils.nextInt()
    private val mHandler = Handler(Looper.myLooper()!!)

    protected fun download(
        fileName: String,
        uri: Uri,
        toContentValues: Function<CSVRecord, ContentValues>
    ) {
        var connection: HttpURLConnection? = null
        try {
            val url = URL("http://legall.benoit.free.fr/nihongo/dico/$fileName")
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return
            }
            loopOnLines(connection, uri, toContentValues)
        } catch (e: Exception) {
            Log.e("AbstractDownloadJobIntentService", "Can not fetch data", e)
        } finally {
            connection?.disconnect()
        }
    }

    @Throws(IOException::class)
    private fun loopOnLines(
        connection: HttpURLConnection?,
        uri: Uri,
        toContentValues: Function<CSVRecord, ContentValues>
    ) {
        BufferedInputStream(connection!!.inputStream).use { `is` ->
            GzipCompressorInputStream(`is`).use { inputStream ->
                TarArchiveInputStream(inputStream).use { tarIn ->
                    var entry: ArchiveEntry
                    while (null != tarIn.nextEntry.also { entry = it }) {
                        if (entry.size < 1) {
                            continue
                        }

                        // parse lines
                        val reader = InputStreamReader(tarIn, StandardCharsets.UTF_8)
                        val parse = CSVFormat.DEFAULT
                            .withHeader()
                            .withSkipHeaderRecord()
                            .parse(reader)
                        var nbValues = 0
                        val contentValues: MutableList<ContentValues> = ArrayList()
                        for (record in parse.records) {
                            contentValues.add(toContentValues.apply(record))
                            nbValues++

                            // todo find best limit before insert loop
                            if (nbValues > 4000) {
                                contentResolver.bulkInsert(uri, contentValues.toTypedArray())
                                nbValues = 0
                                contentValues.clear()
                            }
                        }
                        if (contentValues.isNotEmpty()) {
                            contentResolver.bulkInsert(uri, contentValues.toTypedArray())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val notificationManager = NotificationManagerCompat.from(this)
        val builder = NotificationCompat.Builder(this, "DICO_DOWNLOAD_CHANNEL")
        builder.setContentTitle(getString(notificationTitle))
            .setContentText(getString(R.string.downloads_notify_download_complete))
            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(0, 0, false)
        notificationManager.notify(notificationId, builder.build())
    }

    protected fun notify(progress: Int, resId: Int) {
        val notificationManager = NotificationManagerCompat.from(this)
        val builder = NotificationCompat.Builder(this, "DICO_DOWNLOAD_CHANNEL")
        builder.setContentTitle(getString(notificationTitle))
            .setContentText(getString(resId))
            .setSmallIcon(R.drawable.ic_baseline_download_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSilent(true)
            .setOngoing(true)
            .setProgress(100, progress, false)
        notificationManager.notify(notificationId, builder.build())
    }

    protected fun saveDate(preferenceName: String?) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
            baseContext
        ).edit()
        val now = Instant.now()
        val toEpochMilli = now.toEpochMilli()
        editor.putLong(preferenceName, toEpochMilli)
        editor.apply()
        val intent = Intent()
        intent.action = "com.frogdevelopment.nihongo.dico.Downloaded"
        intent.putExtra("preferenceName", preferenceName)
        sendBroadcast(intent)
    }

    protected fun toast(resId: Int) {
        mHandler.post {
            Toast.makeText(
                this@AbstractDownloadJobIntentService,
                resId,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}