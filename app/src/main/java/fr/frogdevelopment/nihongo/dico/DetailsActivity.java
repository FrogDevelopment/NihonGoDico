package fr.frogdevelopment.nihongo.dico;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
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

	private TextView mLexicon;
	private TextView mGloss;
	private TextView mInfo;
	private ListView mExamples;

	private String kanji;
	private String reading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		TextView mKanji = (TextView) findViewById(R.id.details_kanji);
		TextView mReading = (TextView) findViewById(R.id.details_reading);
		TextView mRomaji = (TextView) findViewById(R.id.details_romaji);
		mLexicon = (TextView) findViewById(R.id.details_lexicon);
		mGloss = (TextView) findViewById(R.id.details_gloss);
		mInfo = (TextView) findViewById(R.id.details_info);
		mExamples = (ListView) findViewById(R.id.details_examples);

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		Bundle args = getIntent().getExtras();

		kanji = args.getString(EntryContract.KANJI);
		mKanji.setText(kanji);
		reading = args.getString(EntryContract.READING);
		mReading.setText(reading);
		mRomaji.setText("<" + KanaToRomaji.convert(reading) + ">");

		getLoaderManager().initLoader(1, args, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
			case 1:
				String[] columns = {SenseContract.POS, SenseContract.FIELD, SenseContract.MISC, SenseContract.DIAL, SenseContract.GLOSS, SenseContract.INFO};
				String selection = SenseContract._ID + "=" + args.getLong(SenseContract._ID);

				return new CursorLoader(this, NihonGoDicoContentProvider.URI_WORD, columns, selection, null, null);
			case 2:
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
			case 1:
				Details item = new Details();
				if (data.moveToNext()) {
					item.pos = data.getString(0);
					item.field = data.getString(1);
					item.misc = data.getString(2);
					item.dial = data.getString(3);
					item.gloss = data.getString(4);
					item.info = data.getString(5);
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

				mGloss.setText(item.gloss);

				// now fetching examples
				getLoaderManager().initLoader(2, null, this);
				break;

			case 2:
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

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
