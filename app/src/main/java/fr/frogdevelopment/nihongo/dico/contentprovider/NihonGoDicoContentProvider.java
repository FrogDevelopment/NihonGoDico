/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.SparseLongArray;

import fr.frogdevelopment.nihongo.dico.utils.InputUtils;

public class NihonGoDicoContentProvider extends ContentProvider {

	private DictionaryOpenHelper mOpenHelper;

	public static final String AUTHORITY = ".NihonGoDicoContentProvider";


	// used for the UriMatcher
	private static final int    WORDS                  = 10;
	private static final int    WORD_ID                = 11;
	private static final String BASE_PATH_WORD         = "word";
	private static final String CONTENT_WORD_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH_WORD + "s";
	private static final String CONTENT_WORD_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_WORD;
	public static final  Uri    URI_WORD               = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_WORD);

	private static final int    SEARCH              = 30;
	private static final String BASE_PATH_SEARCH    = "search";
	private static final String CONTENT_SEARCH_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH_SEARCH;
	public static final  Uri    URI_SEARCH          = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SEARCH);


	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_WORD, WORDS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_WORD + "/#", WORD_ID);

		sURIMatcher.addURI(AUTHORITY, BASE_PATH_SEARCH, SEARCH);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DictionaryOpenHelper(getContext());
		return false;
	}

	@Override
	public String getType(@NonNull Uri uri) {
		int match = sURIMatcher.match(uri);
		switch (match) {

			case WORDS:
				return CONTENT_WORD_TYPE;
			case WORD_ID:
				return CONTENT_WORD_ITEM_TYPE;

			case SEARCH:
				return CONTENT_SEARCH_TYPE;

			default:
				return null;
		}
	}

	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		String groupBy = null;

		queryBuilder.setTables("entry INNER JOIN sense ON (entry._id = sense.entry_id)");
		int uriType = sURIMatcher.match(uri);

		switch (uriType) {

			case WORD_ID:
				queryBuilder.appendWhere(EntryContract._ID + "=" + uri.getLastPathSegment());
				break;

			case SEARCH:
				final String search = selectionArgs[0];
				String fieldName;
				if (InputUtils.containsKanji(search)) {
					fieldName = EntryContract.TABLE_NAME + "." + EntryContract.KANJI;
					sortOrder = EntryContract.KANJI + " ASC";
				} else if (InputUtils.isOnlyKana(search)) {
					fieldName = EntryContract.TABLE_NAME + "." + EntryContract.READING;
					sortOrder = EntryContract.READING + " ASC";
				} else {
					fieldName = SenseContract.TABLE_NAME + "." + SenseContract.GLOSS;
					sortOrder = SenseContract.GLOSS + " ASC";
				}

				queryBuilder.appendWhere(fieldName + " LIKE '%" + search + "%'");
				selectionArgs = null;

				break;

			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(@NonNull Uri uri, ContentValues values) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("");
	}

	// https://eshyu.wordpress.com/2010/08/15/using-sqlite-transactions-with-your-contentprovider/
	@Override
	public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		//standard SQL insert statement, that can be reused
		SQLiteStatement entryStatement = db.compileStatement(EntryContract.SQL_INSERT);
		SQLiteStatement sensesStatement = db.compileStatement(SenseContract.SQL_INSERT);

		int numInserted = 0;
		db.beginTransaction();
		try {
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

						String dial = value.getAsString(SenseContract.DIAL);
						if (misc == null) {
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

			db.setTransactionSuccessful();
			numInserted = values.length;
		} finally {
			db.endTransaction();
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return numInserted;
	}
}
