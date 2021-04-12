package com.frogdevelopment.nihongo.dico.ui.settings;

import android.content.ContentValues;
import android.content.Context;

import org.apache.commons.csv.CSVRecord;

import static com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.SENSE_URI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.DIAL;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.ENTRY_SEQ;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.FIELD;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.GLOSS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INFO;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.MISC;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.POS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.SENSE_SEQ;

class SensesDownLoadTask extends AbstractDownLoadTask {

    private static final String FILE_NAME = "%s_senses.tar";

    SensesDownLoadTask(Context context, DownloadListener listener) {
        super(context, SENSE_URI, listener);
    }

    @Override
    protected String getFileName(String language) {
        return String.format(FILE_NAME, language);
    }

    @Override
    protected ContentValues toContentValues(final CSVRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRY_SEQ, record.get(ENTRY_SEQ));
        contentValues.put(SENSE_SEQ, record.get(SENSE_SEQ));
        contentValues.put(POS, record.get(POS).replaceAll(";", ", "));
        contentValues.put(FIELD, record.get(FIELD).replaceAll(";", ", "));
        contentValues.put(MISC, record.get(MISC).replaceAll(";", ", "));
        contentValues.put(INFO, record.get(INFO));
        contentValues.put(DIAL, record.get(DIAL).replaceAll(";", ", "));
        contentValues.put(GLOSS, record.get("vocabulary"));

        return contentValues;
    }

}
