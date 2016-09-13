/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.SparseLongArray;

public class NihonGoDicoContentProvider extends SearchRecentSuggestionsProvider {

    private DictionaryOpenHelper mOpenHelper;

    public static final String AUTHORITY = ".NihonGoDicoContentProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;


    // used for the UriMatcher
    private static final int URI_MATCH_SUGGEST = 1;

    private static final int WORD_ID = 10;
    private static final String BASE_PATH_WORD = "word";
    private static final String CONTENT_WORD_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_WORD;
    public static final Uri URI_WORD = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_WORD);

    private static final int SEARCH_KANJI = 20;
    private static final String BASE_PATH_SEARCH_KANJI = "search_kanji";
    private static final String CONTENT_SEARCH_KANJI_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_SEARCH_KANJI;
    public static final Uri URI_SEARCH_KANJI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SEARCH_KANJI);

    private static final int SEARCH_KANA = 30;
    private static final String BASE_PATH_SEARCH_KANA = "search_kana";
    private static final String CONTENT_SEARCH_KANA_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_SEARCH_KANA;
    public static final Uri URI_SEARCH_KANA = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SEARCH_KANA);

    private static final int SEARCH_GLOSS = 40;
    private static final String BASE_PATH_SEARCH_GLOSS = "search_gloss";
    private static final String CONTENT_SEARCH_GLOSS_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_SEARCH_GLOSS;
    public static final Uri URI_SEARCH_GLOSS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SEARCH_GLOSS);

    private static final int SENTENCE_ID = 50;
    private static final String BASE_PATH_SENTENCE = "sentence";
    private static final String CONTENT_SENTENCE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_SENTENCE;
    public static final Uri URI_SENTENCE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SENTENCE);


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, URI_MATCH_SUGGEST);

        sURIMatcher.addURI(AUTHORITY, BASE_PATH_WORD, WORD_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SENTENCE, SENTENCE_ID);

        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SEARCH_KANJI, SEARCH_KANJI);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SEARCH_KANA, SEARCH_KANA);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SEARCH_GLOSS, SEARCH_GLOSS);
    }

    public NihonGoDicoContentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DictionaryOpenHelper(getContext());
        return super.onCreate();
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {

            case WORD_ID:
                return CONTENT_WORD_ITEM_TYPE;

            case SENTENCE_ID:
                return CONTENT_SENTENCE_ITEM_TYPE;

            case SEARCH_KANJI:
                return CONTENT_SEARCH_KANJI_TYPE;

            case SEARCH_KANA:
                return CONTENT_SEARCH_KANA_TYPE;

            case SEARCH_GLOSS:
                return CONTENT_SEARCH_GLOSS_TYPE;

            default:
                return super.getType(uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sURIMatcher.match(uri)) {

            case URI_MATCH_SUGGEST:
                return super.query(uri, projection, selection, selectionArgs, sortOrder);

            case WORD_ID:
                queryBuilder.setTables(SenseContract.TABLE_NAME);
                break;

            case SENTENCE_ID:
                queryBuilder.setTables("FTS_EXAMPLE");
                selection = "FTS_EXAMPLE MATCH ?";
                break;

            case SEARCH_KANJI:
            case SEARCH_KANA:
            case SEARCH_GLOSS:
                queryBuilder.setTables("entry INNER JOIN sense ON (entry._id = sense.entry_id)");
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // https://eshyu.wordpress.com/2010/08/15/using-sqlite-transactions-with-your-contentprovider/
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted = 0;

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            switch (sURIMatcher.match(uri)) {

                case WORD_ID:
                    //standard SQL insert statement, that can be reused
                    SQLiteStatement entryStatement = db.compileStatement(EntryContract.SQL_INSERT);
                    SQLiteStatement sensesStatement = db.compileStatement(SenseContract.SQL_INSERT);

                    SparseLongArray entriesId = new SparseLongArray();
                    for (ContentValues value : values) {
                        Integer key = value.getAsInteger("key");

                        String tag = value.getAsString("tag");
                        switch (tag) {
                            case "entry":
                                entryStatement.clearBindings();

                                String kanji = value.getAsString(EntryContract.KANJI);
                                if (kanji == null) {
                                    entryStatement.bindNull(EntryContract.INDEX_KANJI);
                                } else {
                                    entryStatement.bindString(EntryContract.INDEX_KANJI, kanji);
                                }

                                entryStatement.bindString(EntryContract.INDEX_READING, value.getAsString(EntryContract.READING));

                                Long entryId = entryStatement.executeInsert();
                                entriesId.put(key, entryId);
                                break;

                            case "sense":
                                sensesStatement.clearBindings();

                                sensesStatement.bindLong(SenseContract.INDEX_ENTRY_ID, entriesId.get(key));

                                String pos = value.getAsString(SenseContract.POS);
                                if (pos == null) {
                                    sensesStatement.bindNull(SenseContract.INDEX_POS);
                                } else {
                                    sensesStatement.bindString(SenseContract.INDEX_POS, pos);
                                }

                                String field = value.getAsString(SenseContract.FIELD);
                                if (field == null) {
                                    sensesStatement.bindNull(SenseContract.INDEX_FIELD);
                                } else {
                                    sensesStatement.bindString(SenseContract.INDEX_FIELD, field);
                                }

                                String misc = value.getAsString(SenseContract.MISC);
                                if (misc == null) {
                                    sensesStatement.bindNull(SenseContract.INDEX_MISC);
                                } else {
                                    sensesStatement.bindString(SenseContract.INDEX_MISC, misc);
                                }

                                String info = value.getAsString(SenseContract.INFO);
                                if (info == null) {
                                    sensesStatement.bindNull(SenseContract.INDEX_INFO);
                                } else {
                                    sensesStatement.bindString(SenseContract.INDEX_INFO, info);
                                }

                                String dial = value.getAsString(SenseContract.DIAL);
                                if (info == null) {
                                    sensesStatement.bindNull(SenseContract.INDEX_DIAL);
                                } else {
                                    sensesStatement.bindString(SenseContract.INDEX_DIAL, dial);
                                }

                                sensesStatement.bindString(SenseContract.INDEX_GLOSS, value.getAsString(SenseContract.GLOSS));

                                sensesStatement.executeInsert();
                                break;

                            default:
                                // nothing to do
                                break;
                        }
                    }
                    break;

                case SENTENCE_ID:
                    if (values.length == 0) {
                        db.execSQL(ExampleContract.SQL_REBUILD_FTS);
                    } else {
                        //standard SQL insert statement, that can be reused
                        SQLiteStatement sentenceStatement = db.compileStatement(ExampleContract.SQL_INSERT);

                        for (ContentValues value : values) {
                            sentenceStatement.clearBindings();

                            sentenceStatement.bindString(ExampleContract.INDEX_REF, value.getAsString(ExampleContract.REF));
                            sentenceStatement.bindString(ExampleContract.INDEX_LANGUAGE, value.getAsString(ExampleContract.LANGUAGE));
                            sentenceStatement.bindString(ExampleContract.INDEX_SENTENCE, value.getAsString(ExampleContract.SENTENCE));

                            sentenceStatement.executeInsert();
                        }
                    }

                    break;

                default:
                    throw new IllegalArgumentException("Unknow URI : " + uri);
            }

            db.setTransactionSuccessful();
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return numInserted;
    }
}
