package com.frogdevelopment.nihongo.dico.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.frogdevelopment.nihongo.dico.R;

import org.apache.commons.csv.CSVRecord;

import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.CLEAN_SENTENCE_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.REBUILD_SENTENCE_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.SENTENCE_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.JAPANESE;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.LINKING;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.TRANSLATION;

public class SentencesDownloadJobIntentService extends AbstractDownloadJobIntentService {

    static final int JOB_ID = 2000;

    public SentencesDownloadJobIntentService() {
        super(R.string.downloads_toast_sentences);
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SentencesDownloadJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String lang = intent.getStringExtra("lang");

        toast(R.string.downloads_toast_sentences);
        notify(0, R.string.downloads_notify_clean_data);
        getContentResolver().update(CLEAN_SENTENCE_URI, null, null, null);

        notify(50, R.string.downloads_notify_downloading_sentences);
        download(lang + "_sentences.tar", SENTENCE_URI, this::toSentences);

        notify(75, R.string.downloads_notify_rebuild_data);
        getContentResolver().update(REBUILD_SENTENCE_URI, null, null, null);

        saveDate("settings_download_sentences");
    }

    protected ContentValues toSentences(final CSVRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JAPANESE, record.get(JAPANESE));
        contentValues.put(TRANSLATION, record.get(TRANSLATION));
        contentValues.put(LINKING, record.get(LINKING));

        return contentValues;
    }

}
