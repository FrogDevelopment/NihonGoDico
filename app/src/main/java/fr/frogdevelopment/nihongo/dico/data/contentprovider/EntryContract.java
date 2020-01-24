/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class EntryContract implements BaseColumns {

	public static final String TABLE_NAME = "entry";

	public static final String KANJI   = "kanji";
	public static final String READING = "reading";

	public static final int INDEX_KANJI   = 1;
	public static final int INDEX_READING = 2;

	// Queries
	private static final String SQL_CREATE = "CREATE TABLE entry (_id INTEGER PRIMARY KEY AUTOINCREMENT, kanji TEXT, reading TEXT NOT NULL);";
	private static final String SQL_CREATE_FTS = "CREATE VIRTUAL TABLE fts_entry USING fts4 (content='entry', kanji, reading)";

	static final String SQL_INSERT = "INSERT INTO entry (kanji,reading) VALUES (?,?)";
	static final String SQL_REBUILD = "INSERT INTO fts_entry(fts_entry) VALUES('rebuild')";

	private static final String SQL_DROP = "DROP TABLE IF EXISTS entry;";
	private static final String SQL_DROP_FTS = "DROP TABLE IF EXISTS fts_entry;";

	static void create(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
		db.execSQL(SQL_CREATE_FTS);
	}

	static void drop(SQLiteDatabase db) {
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_DROP_FTS);
	}

	// UPDATE
//	static final String UPDATE_2 = "TODO";

}
