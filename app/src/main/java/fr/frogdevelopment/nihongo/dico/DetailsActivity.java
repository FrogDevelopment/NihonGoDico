package fr.frogdevelopment.nihongo.dico;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.frogdevelopment.nihongo.dico.adapters.ExampleAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.ExampleContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Details;
import fr.frogdevelopment.nihongo.dico.entities.Example;

public class DetailsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.details_kanji)
    TextView mKanji;

    @BindView(R.id.details_reading)
    TextView mReading;

    @BindView(R.id.details_lexicon)
    TextView mLexicon;

    @BindView(R.id.details_gloss)
    TextView mGloss;

    @BindView(R.id.details_info)
    TextView mInfo;

    @BindView(R.id.details_show_examples)
    Button mButtonExamples;

    @BindView(R.id.details_examples)
    ListView mExamples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        setActionBar(ButterKnife.findById(this, R.id.toolbar));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();

        mKanji.setText(args.getString(EntryContract.KANJI));
        mReading.setText(args.getString(EntryContract.READING));

        getLoaderManager().initLoader(1, args, this);
    }

    @OnClick(R.id.fab)
    void onFavoriteFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1) {
            String[] columns = {SenseContract.POS, SenseContract.FIELD, SenseContract.MISC, SenseContract.DIAL, SenseContract.GLOSS, SenseContract.INFO};
            String selection = SenseContract._ID + "=" + args.getLong(SenseContract._ID);

            return new CursorLoader(this, NihonGoDicoContentProvider.URI_WORD, columns, selection, null, null);
        } else if (id == 2) {
            String[] columns = {ExampleContract.JAPANESE_SENTENCE, ExampleContract.TRANSLATION_SENTENCE};
            String[] selectionArgs = {mKanji.getText().toString() + " OR " + mReading.getText().toString()};

            return new CursorLoader(this, NihonGoDicoContentProvider.URI_SENTENCE, columns, null, selectionArgs, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 1) {
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
                mLexicon.setText(TextUtils.join(" / ", lexicon)); // fixme rendre clickable => afficher liste lexique
                mLexicon.setVisibility(View.VISIBLE);
            }

            mInfo.setText(item.info);
            if (StringUtils.isBlank(item.info)) {
                mInfo.setVisibility(View.GONE);
            } else {
                mInfo.setVisibility(View.VISIBLE);
            }

            mGloss.setText(item.gloss);
        } else if (loader.getId() == 2) {
            List<Example> examples = new ArrayList<>();

            Pattern patternJapanese = Pattern.compile(mKanji.getText().toString());
            Matcher matcher;
            while (data.moveToNext()) {
                String japanese = data.getString(0);
                Example example = new Example(japanese, data.getString(1));
                matcher = patternJapanese.matcher(japanese);
                while (matcher.find()) {
                    example.matchIndices.add(Pair.of(matcher.start(), matcher.end()));
                }
                examples.add(example);
            }

            mButtonExamples.setVisibility(View.INVISIBLE);
            mExamples.setVisibility(View.VISIBLE);
            mExamples.setAdapter(new ExampleAdapter(this, examples));
        }

        data.close();
        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.details_show_examples)
    void onShowExamples() {

        getLoaderManager().initLoader(2, null, this);
    }
}
