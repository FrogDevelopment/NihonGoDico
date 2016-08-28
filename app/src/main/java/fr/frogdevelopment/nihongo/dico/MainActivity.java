package fr.frogdevelopment.nihongo.dico;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Preview;

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		getMenuInflater().inflate(R.menu.options_menu, menu);

		MenuItem searchMenuItem = menu.findItem(R.id.dico_menu_search);

		SearchView searchView = (SearchView) searchMenuItem.getActionView();
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// Searches the dictionary and displays results for the given query.
			final String query = intent.getStringExtra(SearchManager.QUERY);
			Bundle args = new Bundle();
			args.putString("query", query);
			getLoaderManager().initLoader(LOADER_DICO_ID, args, this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] columns = {EntryContract.KANJI, EntryContract.READING, SenseContract.GLOSS};
		String[] selectionArgs = {args.getString("query")};
		return new CursorLoader(this, NihonGoDicoContentProvider.URI_SEARCH, columns, null, selectionArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 0) {
			// fixme
			Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
		} else {
			List<Preview> previews = new ArrayList<>();
			while (data.moveToNext()) {
				Preview preview = new Preview();
				preview.kanji = data.getString(0);
				preview.reading = data.getString(1);
				preview.gloss = data.getString(2);

				previews.add(preview);
			}

			data.close();
			getLoaderManager().destroyLoader(loader.getId());

			// set the list adapter todo user CursorAdapter
			DicoAdapter mAdapter = new DicoAdapter(this, previews);
			setListAdapter(mAdapter);
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

}
