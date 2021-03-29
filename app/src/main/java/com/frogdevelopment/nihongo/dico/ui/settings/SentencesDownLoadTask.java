package com.frogdevelopment.nihongo.dico.ui.settings;

import android.content.ContentValues;
import android.content.Context;

import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider;
import com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract;

import org.apache.commons.csv.CSVRecord;

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
        contentValues.put(SentenceContract.JAPANESE_REF, record.get(SentenceContract.JAPANESE_REF));
        contentValues.put(SentenceContract.TRANSLATION_REF, record.get(SentenceContract.TRANSLATION_REF));
        contentValues.put(SentenceContract.JAPANESE_REF, record.get(SentenceContract.JAPANESE_REF));
        contentValues.put(SentenceContract.TRANSLATION_SENTENCE, record.get(SentenceContract.TRANSLATION_SENTENCE));
        contentValues.put(SentenceContract.INDICES, record.get(SentenceContract.INDICES));

        return contentValues;
    }

//    @Override
//    protected void notifyRebuild(final ContentResolver contentResolver) {
//        contentResolver.update(URI_REBUILD_EXAMPLE, null, null, null);
//    }
}

