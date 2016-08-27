/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class EntryContract implements BaseColumns {

	public static final String TABLE_NAME = "ENTRY";

	public static final String KANJI   = "KANJI";
	public static final String READING = "READING";

	public static final int INDEX_ID      = 0;
	public static final int INDEX_KANJI   = 1;
	public static final int INDEX_READING = 2;

	public static final String[] COLUMNS = {_ID, KANJI, READING};

	// Queries
	private static final String SQL_CREATE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT NOT NULL);", TABLE_NAME, _ID, KANJI, READING);

	static final String SQL_INSERT = String.format("INSERT INTO %s (%s,%s) VALUES (?,?)", TABLE_NAME, KANJI, READING);

	private static final String SQL_DELETE = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);

	static void create(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	static void delete(SQLiteDatabase db) {
		db.execSQL(SQL_DELETE);
	}

	// UPDATE
//	static final String UPDATE_2 = "TODO";

}
