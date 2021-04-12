package com.frogdevelopment.nihongo.dico.ui.settings;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract;

import org.apache.commons.csv.CSVRecord;

import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.ENTRY_URI;


class EntriesDownLoadTask extends AbstractDownLoadTask {

    private static final String FILE_NAME = "%s_entries.tar";

    EntriesDownLoadTask(Context context, DownloadListener listener) {
        super(context, ENTRY_URI, listener);
    }

    @Override
    protected String getFileName(String language) {
        return String.format(FILE_NAME, language);
    }

    @Override
    protected ContentValues toContentValues(final CSVRecord record) {
        ContentValues contentValues;
        try {
            contentValues = new ContentValues();
            contentValues.put("entry_seq", record.get("entry_seq"));
            contentValues.put(EntryContract.KANJI, record.get(EntryContract.KANJI));
            contentValues.put("kana", record.get("kana"));
            contentValues.put(EntryContract.READING, record.get(EntryContract.READING));
        } catch (Exception e) {
            Log.e("NGD", "Can't read record " + record.toString());
            throw e;
        }

        return contentValues;
    }

}
