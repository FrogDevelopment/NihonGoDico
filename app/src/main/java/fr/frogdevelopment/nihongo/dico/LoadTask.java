package fr.frogdevelopment.nihongo.dico;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Entry;
import fr.frogdevelopment.nihongo.dico.entities.Sense;

class LoadTask extends AsyncTask<Void, String, Void> {

    private ProgressDialog progressDialog;
    private final Context context;

    public LoadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, "ProgressDialog", "Fetching data");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {
        };
        try (InputStream is = context.getResources().openRawResource(R.raw.entries)) {

            publishProgress("récupération fichier");

            List<Entry> entries = mapper.readValue(is, mapType);

            publishProgress("parsing fichier");
            ContentValues[] bulkToInsert;
            List<ContentValues> mValueList = new ArrayList<>();
            int key = 0;
            for (Entry entry : entries) {
                ContentValues entryValues = new ContentValues();
                entryValues.put("tag", "entry");
                entryValues.put("key", ++key);
                entryValues.put(EntryContract.KANJI, entry.kanji);
                entryValues.put(EntryContract.READING, entry.reading);

                mValueList.add(entryValues);

                for (Sense sense : entry.senses) {
                    ContentValues senseValues = new ContentValues();
                    senseValues.put("tag", "sense");
                    senseValues.put("key", key);
                    senseValues.put(SenseContract.POS, StringUtils.join(sense.pos, ", "));
                    senseValues.put(SenseContract.FIELD, StringUtils.join(sense.field, ", "));
                    senseValues.put(SenseContract.MISC, StringUtils.join(sense.misc, ", "));
                    senseValues.put(SenseContract.DIAL, StringUtils.join(sense.dial, ", "));
                    senseValues.put(SenseContract.GLOSS, StringUtils.join(sense.gloss, ", "));

                    mValueList.add(senseValues);
                }
            }

            publishProgress("sauvegarde données");

            // save last lesson and update UI
            bulkToInsert = new ContentValues[mValueList.size()];
            mValueList.toArray(bulkToInsert);
            context.getContentResolver().bulkInsert(NihonGoDicoContentProvider.URI_WORD, bulkToInsert);

            publishProgress("Fini");

        } catch (IOException e) {
            e.printStackTrace(); // fixme
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... text) {
        progressDialog.setMessage(text[0]); // todo progress via % ???
    }

    @Override
    protected void onPostExecute(Void result) {
        progressDialog.dismiss();
    }
}
