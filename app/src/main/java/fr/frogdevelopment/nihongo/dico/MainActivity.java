package fr.frogdevelopment.nihongo.dico;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LoadTask().execute();
    }

    private void initAdapter(List<Entry> entries) {
        // set the list adapter
        DicoAdapter adapter = new DicoAdapter(this, entries);
        setListAdapter(adapter);
    }

    private class LoadTask extends AsyncTask<Void, Void, List<Entry>> {
        @Override
        protected List<Entry> doInBackground(Void... voids) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {
            };
            try (InputStream is = getApplicationContext().getResources().openRawResource(R.raw.entries)) {
                return mapper.readValue(is, mapType);
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            initAdapter(entries);
        }
    }
}
