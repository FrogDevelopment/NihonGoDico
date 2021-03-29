package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

import androidx.annotation.NonNull;

import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.INDEX_KANA;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.INDEX_KANJI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.INDEX_READING;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.KANA;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.KANJI;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.EntryContract.READING;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.DIAL;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.FIELD;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.GLOSS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INDEX_DIAL;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INDEX_FIELD;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INDEX_GLOSS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INDEX_INFO;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INDEX_MISC;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INDEX_POS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.INFO;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.MISC;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SenseContract.POS;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.INDEX_INDICES;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.INDEX_JAPANESE_REF;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.INDEX_JAPANESE_SENTENCE;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.INDEX_TRANSLATION_REF;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.INDEX_TRANSLATION_SENTENCE;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.INDICES;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.JAPANESE_REF;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.JAPANESE_SENTENCE;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.TRANSLATION_REF;
import static com.frogdevelopment.nihongo.dico.data.contentprovider.SentenceContract.TRANSLATION_SENTENCE;

public class NihonGoDicoContentProvider extends SearchRecentSuggestionsProvider {

    private static class DictionaryOpenHelper extends SQLiteOpenHelper {

        // When changing the database schema, increment the database version.
        private static final int    DATABASE_VERSION = 1;
        private static final String DATABASE_NAME    = "NIHON_GO_DICO";

        private DictionaryOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION/*,DatabaseErrorHandler*/);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            EntryContract.create(db);
            SenseContract.create(db);
//            ExampleContract.create(db);
//            FavoritesContract.create(db);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            switch (newVersion) {
//                case 2:
//
//                    EntryContract.drop(db);
//                    SenseContract.drop(db);
//                    ExampleContract.drop(db);
//
//                    EntryContract.create(db);
//                    SenseContract.create(db);
//                    ExampleContract.create(db);
//
//                    // reset
//                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
//                    edit.putBoolean("entries_saved", false);
//                    edit.putBoolean("examples_saved", false);
//                    edit.apply();
//                    break;
//
//                case 3:
//                    FavoritesContract.create(db);
//                    break;
//
//                default:
//                    // nothing to do
//                    break;
//            }
        }
    }

    private DictionaryOpenHelper mOpenHelper;

    public static final String AUTHORITY = "com.frogdevelopment.nihongo.dico.NihonGoDicoContentProvider";
    public final static int    MODE      = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;


    // used for the UriMatcher
    private static final int URI_MATCH_SUGGEST = 1;

    private static final int    ENTRY      = 10;
    private static final String ENTRY_PATH = "ENTRY";
    public static final  Uri    ENTRY_URI  = toUri(ENTRY_PATH);

    private static final int    SENSE      = 11;
    private static final String SENSE_PATH = "SENSE";
    public static final  Uri    SENSE_URI  = toUri(SENSE_PATH);

    private static final int    SENTENCE      = 12;
    private static final String SENTENCE_PATH = "SENTENCE";
    public static final  Uri    SENTENCE_URI  = toUri(SENTENCE_PATH);

    private static final int    CLEAN_DICO      = 20;
    private static final String CLEAN_DICO_PATH = "CLEAN_DICO";
    public static final  Uri    CLEAN_DICO_URI  = toUri(CLEAN_DICO_PATH);

    private static final int    CLEAN_SENTENCE      = 21;
    private static final String CLEAN_SENTENCE_PATH = "CLEAN_SENTENCE";
    public static final  Uri    CLEAN_SENTENCE_URI  = toUri(CLEAN_SENTENCE_PATH);

    private static final int    REBUILD_DICO      = 30;
    private static final String REBUILD_DICO_PATH = "REBUILD_DICO";
    public static final  Uri    REBUILD_DICO_URI  = toUri(REBUILD_DICO_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static Uri toUri(String path) {
        return Uri.parse("content://" + AUTHORITY + "/" + path);
    }

    static {
        sURIMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, URI_MATCH_SUGGEST);

        sURIMatcher.addURI(AUTHORITY, ENTRY_PATH, ENTRY);
        sURIMatcher.addURI(AUTHORITY, SENSE_PATH, SENSE);
        sURIMatcher.addURI(AUTHORITY, SENTENCE_PATH, SENTENCE);
        sURIMatcher.addURI(AUTHORITY, CLEAN_DICO_PATH, CLEAN_DICO);
        sURIMatcher.addURI(AUTHORITY, CLEAN_SENTENCE_PATH, CLEAN_SENTENCE);
        sURIMatcher.addURI(AUTHORITY, REBUILD_DICO_PATH, REBUILD_DICO);
    }

    public NihonGoDicoContentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DictionaryOpenHelper(getContext());
        mOpenHelper.getReadableDatabase();
        return super.onCreate();
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor;
        switch (sURIMatcher.match(uri)) {

            case URI_MATCH_SUGGEST:
                return super.query(uri, projection, selection, selectionArgs, sortOrder);

//            case FAVORITE_ID:
//                String sql_favorites = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id" +
//                        " FROM entry" +
//                        " INNER JOIN sense ON (entry._id = sense.entry_id)" +
//                        " INNER JOIN favorites ON (sense._id = favorites.sense_id)" +
//                        " ORDER BY sense.gloss";
//                cursor = db.rawQuery(sql_favorites, null);
//                break;

            case ENTRY:
                String sql_word = "SELECT sense.pos, sense.field, sense.misc, sense.dial, sense.info, favorites._ID" +
                        " FROM sense " +
                        " LEFT OUTER JOIN favorites ON (sense._id = favorites.sense_id)" +
                        " WHERE sense._ID = ?";
                cursor = db.rawQuery(sql_word, selectionArgs);
                break;

            case SENTENCE:
                String sql = "SELECT japanese_sentence, translation_sentence FROM example WHERE _ID IN " +
                        " (SELECT docid FROM fts_example WHERE fts_example MATCH ?)";
                cursor = db.rawQuery(sql, selectionArgs);
                break;

//            case SEARCH_KANJI:
//                String sql_kanji = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id, favorites._ID FROM entry" +
//                        " INNER JOIN sense ON (entry._id = sense.entry_id)" +
//                        " LEFT OUTER JOIN favorites ON (sense._id = favorites.sense_id)" +
//                        " WHERE entry._ID IN" +
//                        " (SELECT docid FROM fts_entry WHERE kanji MATCH '" + selection + "')";
//                cursor = db.rawQuery(sql_kanji, null);
//                break;

//            case SEARCH_KANA:
//                String sql_kana = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id, favorites._ID FROM entry" +
//                        " INNER JOIN sense ON (entry._id = sense.entry_id)" +
//                        " LEFT OUTER JOIN favorites ON (sense._id = favorites.sense_id)" +
//                        " WHERE entry._ID IN" +
//                        " (SELECT docid FROM fts_entry WHERE reading MATCH '" + selection + "')";
//                cursor = db.rawQuery(sql_kana, null);
//                break;

//            case SEARCH_GLOSS:
//                String sql_gloss = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id, favorites._ID FROM entry" +
//                        " INNER JOIN sense ON (entry._id = sense.entry_id)" +
//                        " LEFT OUTER JOIN favorites ON (sense._id = favorites.sense_id)" +
//                        " WHERE sense._ID IN" +
//                        " (SELECT docid FROM fts_gloss WHERE fts_gloss MATCH '" + selection + "')";
//                cursor = db.rawQuery(sql_gloss, null);
//                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            switch (sURIMatcher.match(uri)) {
                case ENTRY:
                    insertEntries(db, values);
                    break;

                case SENSE:
                    insertSenses(db, values);
                    break;

                case SENTENCE:
                    insertSentences(db, values);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI : " + uri);
            }

            db.setTransactionSuccessful();
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return numInserted;
    }

    private void insertEntries(SQLiteDatabase db, ContentValues[] rows) {
        SQLiteStatement sqLiteStatement = EntryContract.compileInsertStatement(db);
        for (ContentValues row : rows) {
            sqLiteStatement.clearBindings();

            bindStringOrNull(sqLiteStatement, row, EntryContract.ENTRY_SEQ, EntryContract.INDEX_ENTRY_SEQ);
            bindStringOrNull(sqLiteStatement, row, KANJI, INDEX_KANJI);
            bindStringOrNull(sqLiteStatement, row, KANA, INDEX_KANA);
            bindStringOrNull(sqLiteStatement, row, READING, INDEX_READING);

            sqLiteStatement.executeInsert();
        }
    }

    private void insertSenses(SQLiteDatabase db, ContentValues[] rows) {
        SQLiteStatement sqLiteStatement = SenseContract.compileInsertStatement(db);
        for (ContentValues row : rows) {
            sqLiteStatement.clearBindings();

            bindStringOrNull(sqLiteStatement, row, SenseContract.ENTRY_SEQ, SenseContract.INDEX_ENTRY_SEQ);
            bindStringOrNull(sqLiteStatement, row, POS, INDEX_POS);
            bindStringOrNull(sqLiteStatement, row, FIELD, INDEX_FIELD);
            bindStringOrNull(sqLiteStatement, row, MISC, INDEX_MISC);
            bindStringOrNull(sqLiteStatement, row, INFO, INDEX_INFO);
            bindStringOrNull(sqLiteStatement, row, DIAL, INDEX_DIAL);
            bindStringOrNull(sqLiteStatement, row, GLOSS, INDEX_GLOSS);

            sqLiteStatement.executeInsert();
        }
    }

    private void insertSentences(SQLiteDatabase db, ContentValues[] rows) {
        SQLiteStatement sqLiteStatement = SentenceContract.compileInsertStatement(db);
        for (ContentValues row : rows) {
            sqLiteStatement.clearBindings();

            bindStringOrNull(sqLiteStatement, row, JAPANESE_REF, INDEX_JAPANESE_REF);
            bindStringOrNull(sqLiteStatement, row, TRANSLATION_REF, INDEX_TRANSLATION_REF);
            bindStringOrNull(sqLiteStatement, row, JAPANESE_SENTENCE, INDEX_JAPANESE_SENTENCE);
            bindStringOrNull(sqLiteStatement, row, TRANSLATION_SENTENCE, INDEX_TRANSLATION_SENTENCE);
            bindStringOrNull(sqLiteStatement, row, INDICES, INDEX_INDICES);

            sqLiteStatement.executeInsert();
        }
    }

    private static void bindStringOrNull(SQLiteStatement sqLiteStatement, ContentValues values, String key, int index) {
        String value = values.getAsString(key);
        if (value == null) {
            sqLiteStatement.bindNull(index);
        } else {
            sqLiteStatement.bindString(index, value);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sURIMatcher.match(uri)) {
            case CLEAN_DICO:
                EntryContract.clean(db);
                SenseContract.clean(db);
                return 0;

            case REBUILD_DICO:
                EntryContract.reBuild(db);
                SenseContract.reBuild(db);
                return 0;

            case CLEAN_SENTENCE:
                SentenceContract.clean(db);

            default:
                return super.update(uri, values, selection, selectionArgs);
        }

    }

}
