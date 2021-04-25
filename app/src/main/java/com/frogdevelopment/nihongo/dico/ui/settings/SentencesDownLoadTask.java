package com.frogdevelopment.nihongo.dico.ui.settings;

import android.content.ContentValues;
import android.content.Context;

import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider;

import org.apache.commons.csv.CSVRecord;

import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.JAPANESE;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.LINKING;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.TRANSLATION;

class SentencesDownLoadTask extends AbstractDownLoadTask {

    private static final String FILE_NAME = "%s_sentences.tar";

    public SentencesDownLoadTask(Context context, DownloadListener listener) {
        super(context, NihonGoDicoContentProvider.SENTENCE_URI, listener);
    }

    @Override
    protected String getFileName(String language) {
        return String.format(FILE_NAME, language);
    }

    @Override
    protected ContentValues toContentValues(final CSVRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JAPANESE, record.get(JAPANESE));
        contentValues.put(TRANSLATION, record.get(TRANSLATION));
        contentValues.put(LINKING, record.get(LINKING));

        return contentValues;
    }

}

