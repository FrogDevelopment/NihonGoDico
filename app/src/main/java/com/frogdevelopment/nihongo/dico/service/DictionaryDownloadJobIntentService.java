package com.frogdevelopment.nihongo.dico.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.frogdevelopment.nihongo.dico.R;
import com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract;
import com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract;

import org.apache.commons.csv.CSVRecord;

import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.KANA;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.KANJI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.READING;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.CLEAN_DICO_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.ENTRY_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.REBUILD_DICO_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.SENSE_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.DIAL;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.FIELD;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.GLOSS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INFO;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.MISC;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.POS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.SENSE_SEQ;

public class DictionaryDownloadJobIntentService extends AbstractDownloadJobIntentService {

    private static final int JOB_ID = 1000;

    public DictionaryDownloadJobIntentService() {
        super(R.string.downloads_toast_dictionary);
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DictionaryDownloadJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String lang = intent.getStringExtra("lang");

        toast(R.string.downloads_toast_dictionary);
        notify(0, R.string.downloads_notify_clean_data);
        getContentResolver().update(CLEAN_DICO_URI, null, null, null);

        notify(25, R.string.downloads_notify_downloading_entries);
        download(lang + "_entries.tar", ENTRY_URI, this::toEntry);

        notify(50, R.string.downloads_notify_downloading_senses);
        download(lang + "_senses.tar", SENSE_URI, this::toSenses);

        notify(75, R.string.downloads_notify_rebuild_data);
        getContentResolver().update(REBUILD_DICO_URI, null, null, null);

        saveDate("settings_download_entries");
    }

    private ContentValues toEntry(final CSVRecord record) {
        ContentValues contentValues;
        try {
            contentValues = new ContentValues();
            contentValues.put(EntryContract.ENTRY_SEQ, record.get(EntryContract.ENTRY_SEQ));
            contentValues.put(KANJI, record.get(KANJI));
            contentValues.put(KANA, record.get(KANA));
            contentValues.put(READING, record.get(READING));
        } catch (Exception e) {
            Log.e("DictionaryDownloadJobIntentService", "Can't read record " + record.toString());
            throw e;
        }

        return contentValues;
    }

    private ContentValues toSenses(final CSVRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SenseContract.ENTRY_SEQ, record.get(SenseContract.ENTRY_SEQ));
        contentValues.put(SENSE_SEQ, record.get(SENSE_SEQ));
        contentValues.put(POS, record.get(POS).replace(";", ", "));
        contentValues.put(FIELD, record.get(FIELD).replace(";", ", "));
        contentValues.put(MISC, record.get(MISC).replace(";", ", "));
        contentValues.put(INFO, record.get(INFO));
        contentValues.put(DIAL, record.get(DIAL).replace(";", ", "));
        contentValues.put(GLOSS, record.get("vocabulary"));

        return contentValues;
    }

}
