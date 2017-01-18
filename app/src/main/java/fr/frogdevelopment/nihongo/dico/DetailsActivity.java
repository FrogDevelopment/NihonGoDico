package fr.frogdevelopment.nihongo.dico;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.frogdevelopment.nihongo.dico.adapters.ExampleAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Details;
import fr.frogdevelopment.nihongo.dico.entities.Example;
import fr.frogdevelopment.nihongo.dico.utils.KanaToRomaji;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_ID_DATA = 0;
	private static final int LOADER_ID_EXAMPLES = 1;
	public static final String WIKI = "wiki";

	private TextView mLexicon;
	private TextView mInfo;
	private ListView mExamples;
	private View mSpeakView;

	private String kanji;
	private String reading;

	private TextToSpeech mTextToSpeech;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		Bundle args = getIntent().getExtras();

		kanji = args.getString(EntryContract.KANJI);
		reading = args.getString(EntryContract.READING);

		mSpeakView = findViewById(R.id.details_speak);
		mSpeakView.setOnClickListener(view -> {
			Bundle params = new Bundle();
			params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
			mTextToSpeech.speak(reading, TextToSpeech.QUEUE_ADD, params, "UniqueID");
		});

		mTextToSpeech = new TextToSpeech(this, status -> {
			if (status == TextToSpeech.SUCCESS) {
				mTextToSpeech.setLanguage(Locale.JAPAN);
				mSpeakView.setVisibility(View.VISIBLE);
				mTextToSpeech.setOnUtteranceProgressListener(mSpeakProgressListener);
			}
		});

		TextView mKanji = (TextView) findViewById(R.id.details_kanji);
		mKanji.setText(kanji);

		TextView mReading = (TextView) findViewById(R.id.details_reading);
		mReading.setText(reading);

		TextView mRomaji = (TextView) findViewById(R.id.details_romaji);
		mRomaji.setText(KanaToRomaji.convert(reading));

		mLexicon = (TextView) findViewById(R.id.details_lexicon);
		mInfo = (TextView) findViewById(R.id.details_info);
		mExamples = (ListView) findViewById(R.id.details_examples);

		TextView mGloss = (TextView) findViewById(R.id.details_gloss);
		String gloss = args.getString(SenseContract.GLOSS);
		SpannableStringBuilder str = new SpannableStringBuilder(gloss);
		String[] words = gloss.split(",");
		boolean skip = false;
		for (String word : words) {
			if (word.contains("(")) {
				word = word.split("\\(")[0];

				addClickableWord(gloss, str, word);
				skip = true; // skip till end of parenthesis
			} else if (word.endsWith(")")) {
				skip = false; // en dof parenthesis
				continue; // skip this one too, but not the next
			}
			if (!skip) {
				addClickableWord(gloss, str, word);
			}
		}

		mGloss.setText(str);
		mGloss.setMovementMethod(LinkMovementMethod.getInstance());

		getLoaderManager().initLoader(LOADER_ID_DATA, args, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
			case LOADER_ID_DATA:
				String[] columns = {SenseContract.POS, SenseContract.FIELD, SenseContract.MISC, SenseContract.DIAL, SenseContract.INFO};
				String selection = SenseContract._ID + "=" + args.getLong(SenseContract._ID);

				return new CursorLoader(this, NihonGoDicoContentProvider.URI_WORD, columns, selection, null, null);
			case LOADER_ID_EXAMPLES:
				String[] selectionArgs;
				if (StringUtils.isNotBlank(kanji)) {
					selectionArgs = new String[]{kanji};
				} else {
					selectionArgs = new String[]{reading};
				}

				return new CursorLoader(this, NihonGoDicoContentProvider.URI_EXAMPLE, null, null, selectionArgs, null);

			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		int id = loader.getId();
		switch (id) {
			case LOADER_ID_DATA:
				Details item = new Details();
				if (data.moveToNext()) {
					item.pos = data.getString(0);
					item.field = data.getString(1);
					item.misc = data.getString(2);
					item.dial = data.getString(3);
					item.info = data.getString(4);
				}

				List<String> lexicon = new ArrayList<>();
				if (StringUtils.isNotBlank(item.pos)) {
					lexicon.add(item.pos);
				}
				if (StringUtils.isNotBlank(item.field)) {
					lexicon.add(item.field);
				}
				if (StringUtils.isNotBlank(item.misc)) {
					lexicon.add(item.misc);
				}
				if (StringUtils.isNotBlank(item.dial)) {
					lexicon.add(item.dial);
				}

				if (lexicon.isEmpty()) {
					mLexicon.setText("");
					mLexicon.setVisibility(View.GONE);
				} else {
					mLexicon.setText("(" + TextUtils.join(" / ", lexicon) + ")"); // fixme rendre clickable => afficher liste lexique
					mLexicon.setVisibility(View.VISIBLE);
				}

				mInfo.setText(item.info);
				if (StringUtils.isBlank(item.info)) {
					mInfo.setVisibility(View.GONE);
				} else {
					mInfo.setVisibility(View.VISIBLE);
				}

				// now fetching examples
				getLoaderManager().initLoader(LOADER_ID_EXAMPLES, null, this);
				break;

			case LOADER_ID_EXAMPLES:
				List<Example> examples = new ArrayList<>();
				Pattern pattern;
				if (StringUtils.isNotBlank(kanji)) {
					pattern = Pattern.compile(kanji);
				} else {
					pattern = Pattern.compile(reading);
				}
				Matcher matcher;
				while (data.moveToNext()) {
					String japanese = data.getString(0);
					String translation = data.getString(1);
					Example example = new Example(japanese, translation);
					// todo to improve (only exact match) : exemple mouton/laine
					matcher = pattern.matcher(japanese);
					while (matcher.find()) {
						example.matchIndices.add(Pair.of(matcher.start(), matcher.end()));
					}

					examples.add(example);
				}

				mExamples.setAdapter(new ExampleAdapter(this, examples));
				break;
		}

		data.close();
		getLoaderManager().destroyLoader(id);
	}

	private void addClickableWord(String gloss, SpannableStringBuilder str, String word) {
		String trim = word.trim();
		int start = gloss.indexOf(trim);
		int end = start + trim.length();
		str.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View view) {
				onClickWord(trim);
			}
		}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	private void onClickWord(CharSequence word) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(WIKI);
		intent.putExtra(SearchManager.QUERY, word);

		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		if (mTextToSpeech != null) {
			mTextToSpeech.shutdown();
			mTextToSpeech = null;
		}
		super.onDestroy();
	}

	private final UtteranceProgressListener mSpeakProgressListener = new UtteranceProgressListener() {

		@Override
		public void onStart(String utteranceId) {
			mSpeakView.setClickable(false);
		}

		@Override
		public void onDone(String utteranceId) {
			mSpeakView.setClickable(true);
		}

		@Override
		public void onError(String utteranceId) {

		}
	};
}
