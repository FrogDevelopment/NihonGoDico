/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class SenseContract implements BaseColumns {

	public static final String TABLE_NAME = "SENSE";

	public static final String ENTRY_ID = "ENTRY_ID";
	public static final String POS      = "POS";
	public static final String FIELD    = "FIELD";
	public static final String MISC     = "MISC";
	public static final String DIAL     = "DIAL";
	public static final String GLOSS    = "GLOSS";

	public static final int INDEX_ID       = 0;
	public static final int INDEX_ENTRY_ID = 1;
	public static final int INDEX_POS      = 2;
	public static final int INDEX_FIELD    = 3;
	public static final int INDEX_MISC     = 4;
	public static final int INDEX_DIAL     = 5;
	public static final int INDEX_GLOSS    = 6;

	public static final String[] COLUMNS = {_ID, ENTRY_ID, POS, FIELD, MISC, DIAL, GLOSS};

	// Queries
	private static final String SQL_CREATE = String.format(
			"CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT NOT NULL, FOREIGN KEY(%s) REFERENCES %s(%s));",
			TABLE_NAME, _ID, ENTRY_ID, POS, FIELD, MISC, DIAL, GLOSS, ENTRY_ID, EntryContract.TABLE_NAME, EntryContract._ID);

	static final String SQL_INSERT = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)", TABLE_NAME, ENTRY_ID, POS, FIELD, MISC, DIAL, GLOSS);

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
