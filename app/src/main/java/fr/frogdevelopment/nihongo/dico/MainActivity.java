package fr.frogdevelopment.nihongo.dico;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Entry;
import fr.frogdevelopment.nihongo.dico.entities.Preview;
import fr.frogdevelopment.nihongo.dico.entities.Sense;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_DICO_ID = 100;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

		loadData();
	}

	private void loadData() {
		getLoaderManager().initLoader(LOADER_DICO_ID, null, this);
	}

	private void initAdapter(List<Preview> previews) {
		// set the list adapter
		DicoAdapter adapter = new DicoAdapter(this, previews);
		setListAdapter(adapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] columns = {EntryContract.KANJI, EntryContract.READING, SenseContract.GLOSS};
		return new CursorLoader(this, NihonGoContentProvider.URI_WORD, columns, null, null, EntryContract.READING + " ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 0) {
			Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
//			new LoadTask().execute();
		} else {
			Toast.makeText(this, "Data present :)", Toast.LENGTH_SHORT).show();

			List<Preview> previews = new ArrayList<>();
			while (data.moveToNext()) {
				Preview preview = new Preview();
				preview.kanji = data.getString(0);
				preview.reading = data.getString(1);
				preview.gloss = data.getString(2);

				previews.add(preview);
			}

			initAdapter(previews);

			getLoaderManager().destroyLoader(loader.getId());
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("Main Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... voids) {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {
			};
			try (InputStream is = getApplicationContext().getResources().openRawResource(R.raw.entries)) {

				// récupération fichier
				// publishProgress();

				List<Entry> entries = mapper.readValue(is, mapType);
				List<Preview> previews = new ArrayList<>();

				// parsing fichier
				// publishProgress();
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
						senseValues.put(SenseContract.POS, StringUtils.join(sense.pos, ","));
						senseValues.put(SenseContract.FIELD, StringUtils.join(sense.field, ","));
						senseValues.put(SenseContract.MISC, StringUtils.join(sense.misc, ","));
						senseValues.put(SenseContract.DIAL, StringUtils.join(sense.dial, ","));
						senseValues.put(SenseContract.GLOSS, StringUtils.join(sense.gloss, ","));

						mValueList.add(senseValues);
					}
				}

				// sauvegarde données
				// publishProgress();

				// save last lesson and update UI
				bulkToInsert = new ContentValues[mValueList.size()];
				mValueList.toArray(bulkToInsert);
				getApplicationContext().getContentResolver().bulkInsert(NihonGoContentProvider.URI_WORD, bulkToInsert);

				// fini
				// publishProgress();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			loadData();
		}
	}
}
