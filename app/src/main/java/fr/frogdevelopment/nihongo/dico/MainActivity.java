package fr.frogdevelopment.nihongo.dico;

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
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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

import fr.frogdevelopment.nihongo.dico.adapters.DicoAdapter;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByGlossAdapter;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByKanaAdapter;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByKanjiAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.downloads.DownloadsActivity;
import fr.frogdevelopment.nihongo.dico.entities.Preview;
import fr.frogdevelopment.nihongo.dico.utils.InputUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int RC_DATA_CHECK_CODE   = 735;
	private static final int LOADER_DICO_ID_KANJI = 100;
	private static final int LOADER_DICO_ID_KANA  = 200;
	private static final int LOADER_DICO_ID_GLOSS = 300;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
	private ListView        mListView;
	private DicoAdapter     mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, RC_DATA_CHECK_CODE);

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

		boolean data_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("entries_saved", false);
		if (!data_saved) {
			startActivity(new Intent(this, DownloadsActivity.class));
		} else {
			handleIntent(getIntent());
		}

		SearchView searchView = (SearchView) findViewById(R.id.search_field);
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// Find EditText view
		EditText searchText = (EditText) findViewById(R.id.search_src_text);
		// Get the search close button image view
		ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
		// Set on click listener
		closeButton.setOnClickListener(view -> {
			// Clear the text from EditText view
			searchText.setText(null);
			// Clear query
			searchView.setQuery("", false);
			// Clear the results
			if (mAdapter != null) {
				mAdapter.clear();
				mAdapter.notifyDataSetChanged();
			}
		});

		mListView = (ListView) findViewById(R.id.entries_list);
		mListView.setOnItemClickListener((adapterView, view, i, l) -> onItemClick(i));
	}

	private void onItemClick(int position) {
		Preview item = mAdapter.getItem(position);
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(EntryContract.KANJI, item.kanji);
		intent.putExtra(EntryContract.READING, item.reading);
		intent.putExtra(SenseContract._ID, item.sense_id);

		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RC_DATA_CHECK_CODE) {
			// success, create the TTS instance
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// check japanese available
				ArrayList<String> availableVoices = data.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES);
				String jpnTag = "jpn-jpn"; // fixme
				if (!availableVoices.contains(jpnTag)) {
					// fixme
				}
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		getMenuInflater().inflate(R.menu.options_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.dico_menu_download:
				startActivity(new Intent(this, DownloadsActivity.class));
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (action != null) {
			switch (action) {
				case Intent.ACTION_SEARCH:
				case DetailsActivity.WIKI:
					// Searches the dictionary and displays results for the given query.
					String query = intent.getStringExtra(SearchManager.QUERY);
					launchQueryFor(query);
					break;
			}
		}
	}

	private void launchQueryFor(String query) {
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
	}

	private String query;

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] columns = {EntryContract.KANJI, EntryContract.READING, SenseContract.GLOSS, "sense._id"};
		query = args.getString("query", "");

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
				uri = NihonGoDicoContentProvider.URI_SEARCH_GLOSS;
				String escapeString = query.replace("'", "''");
				selection = SenseContract.TABLE_NAME + "." + SenseContract.GLOSS + " LIKE '%" + escapeString + "%'";
				break;
			default:
				return null;
		}

		return new CursorLoader(this, uri, columns, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		List<Preview> previews = new ArrayList<>();
		Pattern pattern = Pattern.compile(query.toLowerCase());
		int loaderId = loader.getId();
		while (data.moveToNext()) {
			Preview preview = new Preview();
			preview.kanji = data.getString(0);
			preview.reading = data.getString(1);
			preview.gloss = data.getString(2);
			preview.sense_id = data.getLong(3);

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

		// adapter by research type
		switch (loaderId) {
			case LOADER_DICO_ID_KANJI:
				mAdapter = new ResearchByKanjiAdapter(this, previews);
				break;
			case LOADER_DICO_ID_KANA:
				mAdapter = new ResearchByKanaAdapter(this, previews);
				break;
			case LOADER_DICO_ID_GLOSS:
			default:
				mAdapter = new ResearchByGlossAdapter(this, previews);
		}

		mListView.setAdapter(mAdapter);

		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, NihonGoDicoContentProvider.AUTHORITY, NihonGoDicoContentProvider.MODE);
		suggestions.saveRecentQuery(query, String.valueOf(previews.size() + " results"));
	}

	private void computeSimilarity(Pattern pattern, Preview preview, String text) {
		// keep max similarity
		preview.similarity = 0;
		String[] values = text.split(", ");
		for (String value : values) {
			double v = InputUtils.computeSimilarity(query, value);
			if (v > preview.similarity) {
				preview.similarity = v;
			}
		}

		// if only 1 word and match => has to be first multiple values
		if (values.length == 1 && preview.similarity == 1) {
			preview.similarity = 2;
		}

		// get all regions which match
		Matcher matcher = pattern.matcher(text.toLowerCase());
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
