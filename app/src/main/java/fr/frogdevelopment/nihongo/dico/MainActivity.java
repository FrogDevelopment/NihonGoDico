package fr.frogdevelopment.nihongo.dico;

import android.app.AlertDialog;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

	private static final int RC_DATA_CHECK_CODE = 735;
	private static final int LOADER_DICO_ID_KANJI = 100;
	private static final int LOADER_DICO_ID_KANA = 200;
	private static final int LOADER_DICO_ID_GLOSS = 300;

	private static final String REGEX_SEARCH_SPLIT = "\\+|-|\\?";

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
	private ProgressBar mProgressBar;
	private ListView mListView;
	private DicoAdapter mAdapter;
	private SearchView.SearchAutoComplete mSearchAutoComplete;

	private String query;

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

		SearchView searchView = (SearchView) findViewById(R.id.search_field);
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// Find EditText view
		mSearchAutoComplete = (SearchView.SearchAutoComplete) findViewById(R.id.search_src_text);
		// Get the search close button image view
		ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
		// Set on click listener
		closeButton.setOnClickListener(view -> {
			// Clear the text from EditText view
			mSearchAutoComplete.setText(null);
			// Clear query
			searchView.setQuery("", false);
			// Clear the results
			if (mAdapter != null) {
				mAdapter.clear();
				mAdapter.notifyDataSetChanged();
			}
		});

		mProgressBar = (ProgressBar) findViewById(R.id.search_progress);

		mListView = (ListView) findViewById(R.id.entries_list);
		mListView.setOnItemClickListener((adapterView, view, i, l) -> onItemClick(i));

		boolean data_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("entries_saved", false);
		if (!data_saved) {
			startActivity(new Intent(this, DownloadsActivity.class));
		} else {
			handleIntent(getIntent());
		}

		if (!isTaskRoot()) {
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
	}

	private void onItemClick(int position) {
		Preview item = mAdapter.getItem(position);
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(EntryContract.KANJI, item.kanji);
		intent.putExtra(EntryContract.READING, item.reading);
		intent.putExtra(SenseContract.GLOSS, item.gloss);
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
					mSearchAutoComplete.setText(query, false);
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mProgressBar.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.INVISIBLE);

		final Uri uri;
		switch (id) {
			case LOADER_DICO_ID_KANJI:
				uri = NihonGoDicoContentProvider.URI_SEARCH_KANJI;
				break;
			case LOADER_DICO_ID_KANA:
				uri = NihonGoDicoContentProvider.URI_SEARCH_KANA;
				break;
			case LOADER_DICO_ID_GLOSS:
				uri = NihonGoDicoContentProvider.URI_SEARCH_GLOSS;
				break;
			default:
				return null;
		}

		query = args.getString("query", "");

		// query by word1+word2+...
		String[] searches = query.split(REGEX_SEARCH_SPLIT);

		char charAt;
		String selection = "";
		// if other words, check either include (+) or exclude (-) from query
		for (String search : searches) { // start 1, as first word already proceed
			if (TextUtils.isEmpty(search)) {
				continue;
			}

			search = search.trim();

			// check the character in front of word to know if inclusion or exclusion
			int startOfWord = query.indexOf(search);

			if (startOfWord > 0) {
				do {
					charAt = query.charAt(--startOfWord);
				} while (Character.isWhitespace(charAt));

				switch (charAt) {
					case '+': // AND implicit
						selection += " ";
						break;
					case '?': // OR
						selection += " OR ";
						break;
					case '-': // NOT
						selection += " -";
						break;
				}
			}

			if (search.split("\\s").length > 1) { // phrase query => enclosing in double quotes
				selection += "\"" + cleanWord(search) + "\"";
			} else {
				selection += cleanWord(search);
			}
		}

		return new CursorLoader(this, uri, null, selection, null, null);
	}

	private static String cleanWord(String word) {
		return word
				.trim() // remove leading and trailing spaces
				.replace("'", "''") // replace ['] by [''] for sql syntax
				; // todo other ?
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		List<Preview> previews = new ArrayList<>();

		String[] searches = query.split(REGEX_SEARCH_SPLIT);

		List<Pattern> patterns = new ArrayList<>();
		for (String search : searches) {
			// check the character in front of word to know if inclusion or exclusion
			int indexOfWord = query.indexOf(search);
			if (indexOfWord > 0) {
				char charAt;
				do {
					charAt = query.charAt(--indexOfWord);
				} while (Character.isWhitespace(charAt));

				switch (charAt) {
					case '+':
					case '?':
						patterns.add(Pattern.compile(Pattern.quote(search.trim().toLowerCase())));
						break;
				}
			} else {
				patterns.add(Pattern.compile(Pattern.quote(search.trim().toLowerCase())));
			}
		}

		int loaderId = loader.getId();
		while (data.moveToNext()) {
			Preview preview = new Preview();
			preview.kanji = data.getString(0);
			preview.reading = data.getString(1);
			preview.gloss = data.getString(2);
			preview.sense_id = data.getLong(3);

			String text;
			switch (loaderId) {
				case LOADER_DICO_ID_KANJI:
					text = preview.kanji;
					break;
				case LOADER_DICO_ID_KANA:
					text = preview.reading;
					break;
				case LOADER_DICO_ID_GLOSS:
				default:
					text = preview.gloss;
			}

			computeSimilarity(patterns, preview, text);

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

		mProgressBar.setVisibility(View.INVISIBLE);
		mListView.setVisibility(View.VISIBLE);
	}

	private void computeSimilarity(List<Pattern> patterns, Preview preview, String text) {
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
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(text.toLowerCase());
			while (matcher.find()) {
				preview.matchIndices.add(Pair.of(matcher.start(), matcher.end()));
			}
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
		if (isTaskRoot()) {
			new AlertDialog.Builder(this)
					.setMessage(R.string.exit_message)
					.setPositiveButton(R.string.exit_yes, (dialog, which) -> finish())
					.setNegativeButton(R.string.exit_no, null)
					.show();
		} else {
			super.onBackPressed();
		}
	}

}
