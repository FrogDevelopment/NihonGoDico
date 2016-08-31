package fr.frogdevelopment.nihongo.dico;

import android.app.AlertDialog;
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
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ListAdapter;
import android.widget.SearchView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnItemClick;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByGlossAdapter;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByKanaAdapter;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByKanjiAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Preview;
import fr.frogdevelopment.nihongo.dico.utils.InputUtils;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_DICO_ID_KANJI = 100;
	private static final int LOADER_DICO_ID_KANA  = 200;
	private static final int LOADER_DICO_ID_GLOSS = 300;

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

		ButterKnife.bind(this);

		// init mToolbar
		setActionBar(ButterKnife.findById(this, R.id.toolbar));

//		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//			}
//		});

		handleIntent(getIntent());
	}

	@OnItemClick(android.R.id.list)
	void onItemClick(int position) {
		Preview item = (Preview) getListAdapter().getItem(position);
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(EntryContract.KANJI, item.kanji);
		intent.putExtra(EntryContract.READING, item.reading);
		intent.putExtra(SenseContract.ENTRY_ID, item.entry_id);

		startActivity(intent);
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
			String query = intent.getStringExtra(SearchManager.QUERY);

			// https://developer.android.com/guide/topics/search/adding-recent-query-suggestions.html
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, NihonGoDicoContentProvider.AUTHORITY, NihonGoDicoContentProvider.MODE);
			suggestions.saveRecentQuery(query, null);

			// fixme proposer la possibilité d'effacer l'history
//            suggestions.clearHistory();

			Bundle args = new Bundle();
			args.putString("query", query.trim());

			int loaderId;
			if (InputUtils.containsKanji(query)) {
				loaderId = LOADER_DICO_ID_KANJI;
			} else if (InputUtils.isOnlyKana(query)) {
				loaderId = LOADER_DICO_ID_KANA;
			} else {
				loaderId = LOADER_DICO_ID_GLOSS;
			}

			getLoaderManager().initLoader(loaderId, args, this);
		} else {
			boolean data_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("data_saved", false);
			if (!data_saved) {
				// fixme
				new AlertDialog.Builder(this)
						.setMessage("no data found. Download it ?")
						.setPositiveButton(android.R.string.ok, (dialog, id) -> new LoadTask(MainActivity.this).execute())
						.setNegativeButton(android.R.string.cancel, null)
						.create()
						.show();
			}
		}
	}

	private String query;

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] columns = {EntryContract.KANJI, EntryContract.READING, SenseContract.GLOSS, SenseContract.ENTRY_ID};
		query = args.getString("query");

		Uri uri;
		String selection;
		switch (id) {
			case LOADER_DICO_ID_KANJI:
				uri = NihonGoDicoContentProvider.URI_SEARCH_KANJI;
				selection = EntryContract.TABLE_NAME + "." + EntryContract.KANJI + " LIKE '%" + query + "%'";
				break;
			case LOADER_DICO_ID_KANA:
				uri = NihonGoDicoContentProvider.URI_SEARCH_KANA;
				selection = EntryContract.TABLE_NAME + "." + EntryContract.READING + " LIKE '%" + query + "%'";
				break;
			case LOADER_DICO_ID_GLOSS:
			default:
				uri = NihonGoDicoContentProvider.URI_SEARCH_GLOSS;
				selection = SenseContract.TABLE_NAME + "." + SenseContract.GLOSS + " LIKE '%" + query + "%'";
		}

		return new CursorLoader(this, uri, columns, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		List<Preview> previews = new ArrayList<>();
		Pattern pattern = Pattern.compile(query);
		int loaderId = loader.getId();
		while (data.moveToNext()) {
			Preview preview = new Preview();
			preview.kanji = data.getString(0);
			preview.reading = data.getString(1);
			preview.gloss = data.getString(2);
			preview.entry_id = data.getLong(3);

			switch (loaderId) {
				case LOADER_DICO_ID_KANJI:
					computeSimilarity(pattern, preview, preview.kanji);
					break;
				case LOADER_DICO_ID_KANA:
					computeSimilarity(pattern, preview, preview.reading);
					break;
				case LOADER_DICO_ID_GLOSS:
				default:
					computeSimilarity(pattern, preview, preview.gloss);
			}

			previews.add(preview);
		}

		// sort by descending similarity score
		Collections.sort(previews, (p1, p2) -> Double.compare(p2.similarity, p1.similarity));

		data.close();
		getLoaderManager().destroyLoader(loaderId);

		// set the list adapter
		ListAdapter adapter;

		// adapter by research type
		switch (loaderId) {
			case LOADER_DICO_ID_KANJI:
				adapter = new ResearchByKanjiAdapter(this, previews);
				break;
			case LOADER_DICO_ID_KANA:
				adapter = new ResearchByKanaAdapter(this, previews);
				break;
			case LOADER_DICO_ID_GLOSS:
			default:
				adapter = new ResearchByGlossAdapter(this, previews);
		}

		setListAdapter(adapter);

		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, NihonGoDicoContentProvider.AUTHORITY, NihonGoDicoContentProvider.MODE);
		suggestions.saveRecentQuery(query, String.valueOf(previews.size() + " results"));
	}

	private void computeSimilarity(Pattern pattern, Preview preview, String text) {
		// keep max similarity
		preview.similarity = 0;
		for (String value : text.split(", ")) {
			double v = InputUtils.computeSimilarity(query, value);
			if (v > preview.similarity) {
				preview.similarity = v;
			}
		}

		// get all regions which match
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			preview.matchIndices.add(Pair.of(matcher.start(), matcher.end()));
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

	@Override
	public void onBackPressed() {
		Snackbar.make(findViewById(R.id.activity_main), "Are you sure you want to exit ?", Snackbar.LENGTH_LONG)
				.setAction(android.R.string.ok, v -> finish())
				.show();
	}

}
