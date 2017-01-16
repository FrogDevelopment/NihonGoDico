/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.SparseLongArray;

public class NihonGoDicoContentProvider extends SearchRecentSuggestionsProvider {

	private class DictionaryOpenHelper extends SQLiteOpenHelper {

		// When changing the database schema, increment the database version.
		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "NIHON_GO_DICO";

		private DictionaryOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION/*,DatabaseErrorHandler*/);
		}

		// Creating Tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			EntryContract.create(db);
			SenseContract.create(db);
			ExampleContract.create(db);
		}

		// Upgrading database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (newVersion == 2) {

				EntryContract.drop(db);
				SenseContract.drop(db);
				ExampleContract.drop(db);

				EntryContract.create(db);
				SenseContract.create(db);
				ExampleContract.create(db);

				// reset
				SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
				edit.putBoolean("entries_saved", false);
				edit.putBoolean("examples_saved", false);
				edit.apply();
			}
		}
	}

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

	private static final int EXAMPLE = 50;
	private static final String BASE_PATH_EXAMPLE = "example";
	private static final String CONTENT_EXAMPLE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_EXAMPLE;
	public static final Uri URI_EXAMPLE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_EXAMPLE);

	private static final int REBUILD_DICO = 60;
	private static final String BASE_PATH_REBUILD_DICO = "REBUILD_DICO";
	private static final String CONTENT_REBUILD_DICO_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_REBUILD_DICO;
	public static final Uri URI_REBUILD_DICO = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_REBUILD_DICO);

	private static final int REBUILD_EXAMPLE = 70;
	private static final String BASE_PATH_REBUILD_EXAMPLE = "REBUILD_EXAMPLE";
	private static final String CONTENT_REBUILD_EXAMPLE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_REBUILD_EXAMPLE;
	public static final Uri URI_REBUILD_EXAMPLE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_REBUILD_EXAMPLE);


	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, URI_MATCH_SUGGEST);

		sURIMatcher.addURI(AUTHORITY, BASE_PATH_WORD, WORD_ID);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_EXAMPLE, EXAMPLE);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_REBUILD_DICO, REBUILD_DICO);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_REBUILD_EXAMPLE, REBUILD_EXAMPLE);

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

			case EXAMPLE:
				return CONTENT_EXAMPLE_TYPE;

			case SEARCH_KANJI:
				return CONTENT_SEARCH_KANJI_TYPE;

			case SEARCH_KANA:
				return CONTENT_SEARCH_KANA_TYPE;

			case SEARCH_GLOSS:
				return CONTENT_SEARCH_GLOSS_TYPE;

			case REBUILD_DICO:
				return CONTENT_REBUILD_DICO_TYPE;

			case REBUILD_EXAMPLE:
				return CONTENT_REBUILD_EXAMPLE_TYPE;

			default:
				return super.getType(uri);
		}
	}

	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor;
		switch (sURIMatcher.match(uri)) {

			case URI_MATCH_SUGGEST:
				return super.query(uri, projection, selection, selectionArgs, sortOrder);

			case WORD_ID:
				queryBuilder.setTables(SenseContract.TABLE_NAME);
				cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
				break;

			case EXAMPLE:
				String sql = "SELECT japanese_sentence, translation_sentence FROM example WHERE _ID IN " +
//                String sql = "SELECT japanese_sentence, indices FROM example WHERE _ID IN " +
						" (SELECT docid FROM fts_example WHERE fts_example MATCH ?)";
				cursor = db.rawQuery(sql, selectionArgs);
				break;

			case SEARCH_KANJI:
				String sql_kanji = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id FROM entry" +
						" INNER JOIN sense ON (entry._id = sense.entry_id)" +
						" WHERE entry._ID IN" +
						" (SELECT docid FROM fts_entry WHERE kanji MATCH '" + selection + "')";
				cursor = db.rawQuery(sql_kanji, null);
				break;

			case SEARCH_KANA:
				String sql_kana = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id FROM entry" +
						" INNER JOIN sense ON (entry._id = sense.entry_id)" +
						" WHERE entry._ID IN" +
						" (SELECT docid FROM fts_entry WHERE reading MATCH '" + selection + "')";
				cursor = db.rawQuery(sql_kana, null);
				break;

			case SEARCH_GLOSS:
				String sql_gloss = "SELECT entry.kanji, entry.reading, sense.gloss, sense._id FROM entry" +
						" INNER JOIN sense ON (entry._id = sense.entry_id)" +
						" WHERE sense._ID IN" +
						" (SELECT docid FROM fts_gloss WHERE fts_gloss MATCH '" + selection + "')";
				cursor = db.rawQuery(sql_gloss, null);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

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

				case EXAMPLE:
					//standard SQL insert statement, that can be reused
					SQLiteStatement sentenceStatement = db.compileStatement(ExampleContract.SQL_INSERT);

					for (ContentValues value : values) {
						sentenceStatement.clearBindings();

						sentenceStatement.bindString(ExampleContract.INDEX_JAPANESE_REF, value.getAsString(ExampleContract.JAPANESE_REF));
						sentenceStatement.bindString(ExampleContract.INDEX_TRANSLATION_REF, value.getAsString(ExampleContract.TRANSLATION_REF));
						sentenceStatement.bindString(ExampleContract.INDEX_JAPANESE_SENTENCE, value.getAsString(ExampleContract.JAPANESE_SENTENCE));
						sentenceStatement.bindString(ExampleContract.INDEX_TRANSLATION_SENTENCE, value.getAsString(ExampleContract.TRANSLATION_SENTENCE));
						sentenceStatement.bindString(ExampleContract.INDEX_INDICES, value.getAsString(ExampleContract.INDICES));

						sentenceStatement.executeInsert();
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

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		switch (sURIMatcher.match(uri)) {
			case REBUILD_DICO:
				db.execSQL(EntryContract.SQL_REBUILD);
				db.execSQL(SenseContract.SQL_REBUILD);
				return 0;

			case REBUILD_EXAMPLE:
				db.execSQL(ExampleContract.SQL_REBUILD);
				return 0;

			default:
				return super.update(uri, values, selection, selectionArgs);
		}

	}
}
