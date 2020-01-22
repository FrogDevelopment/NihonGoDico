package fr.frogdevelopment.nihongo.dico;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.frogdevelopment.nihongo.dico.adapters.ExampleAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.FavoritesContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Details;
import fr.frogdevelopment.nihongo.dico.entities.Example;
import fr.frogdevelopment.nihongo.dico.utils.KanaToRomaji;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String WIKI = "wiki";
    private static final int LOADER_ID_DATA = 0;
    private static final int LOADER_ID_EXAMPLES = 1;
    private TextView mLexicon;
    private TextView mInfo;
    private ListView mExamples;
    private ImageView mFavorite;

    private String kanji;
    private String reading;
    private long senseId;
    private Long favoriteId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle args = getIntent().getExtras();

        kanji = args.getString(EntryContract.KANJI);
        reading = args.getString(EntryContract.READING);

        mFavorite = findViewById(R.id.details_favorite);
        mFavorite.setOnClickListener(view -> {
            if (favoriteId != null) {
                deleteFavorite();
            } else {
                insertFavorite();
            }
        });

        TextView mKanji = findViewById(R.id.details_kanji);
        mKanji.setText(kanji);

        TextView mReading = findViewById(R.id.details_reading);
        mReading.setText(reading);

        TextView mRomaji = findViewById(R.id.details_romaji);
        mRomaji.setText(KanaToRomaji.convert(reading));

        mLexicon = findViewById(R.id.details_lexicon);
        mInfo = findViewById(R.id.details_info);
        mExamples = findViewById(R.id.details_examples);

        TextView mGloss = findViewById(R.id.details_gloss);
        String gloss = args.getString(SenseContract.GLOSS);
        SpannableStringBuilder str = new SpannableStringBuilder(gloss);
        String[] words = gloss.split(",");
        boolean skip = false;
        for (String word : words) {
            if (word.contains("(")) {
                addClickableWord(gloss, str, word.split("\\(")[0]);
                skip = true; // skip till end of parenthesis
            }

            if (word.endsWith(")")) {
                skip = false; // end of parenthesis
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
        String[] selectionArgs;
        switch (id) {
            case LOADER_ID_DATA:
                String[] columns = {SenseContract.POS, SenseContract.FIELD, SenseContract.MISC, SenseContract.DIAL, SenseContract.INFO};
                senseId = args.getLong(SenseContract._ID);
                selectionArgs = new String[]{String.valueOf(senseId)};

                return new CursorLoader(this, NihonGoDicoContentProvider.URI_WORD, columns, null, selectionArgs, null);
            case LOADER_ID_EXAMPLES:
                if (isNotBlank(kanji)) {
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
                    if (!data.isNull(5)) {
                        favoriteId = data.getLong(5);
                    }
                    handleFavoriteResource();
                }

                List<String> lexicon = new ArrayList<>();
                if (isNotBlank(item.pos)) {
                    lexicon.add(item.pos);
                }
                if (isNotBlank(item.field)) {
                    lexicon.add(item.field);
                }
                if (isNotBlank(item.misc)) {
                    lexicon.add(item.misc);
                }
                if (isNotBlank(item.dial)) {
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
                if (isBlank(item.info)) {
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
                if (isNotBlank(kanji)) {
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
            public void onClick(@NonNull View view) {
                onClickWord(trim);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void onClickWord(CharSequence word) {
        Intent intent = new Intent(this, OldMainActivity.class);
        intent.setAction(WIKI);
        intent.putExtra(SearchManager.QUERY, word);

        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertFavorite() {
        final ContentValues values = new ContentValues();
        values.put(FavoritesContract.SENSE_ID, senseId);

        Uri uri = getContentResolver().insert(NihonGoDicoContentProvider.URI_FAVORITE, values);

        favoriteId = ContentUris.parseId(uri);
        handleFavoriteResource();
    }

    private void deleteFavorite() {
        final String where = FavoritesContract.SENSE_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(senseId)};

        getContentResolver().delete(NihonGoDicoContentProvider.URI_FAVORITE, where, selectionArgs);

        favoriteId = null;
        handleFavoriteResource();
    }

    private void handleFavoriteResource() {
        if (favoriteId == null) {
            mFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            mFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }
}
