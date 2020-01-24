/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class SenseContract implements BaseColumns {

	public static final String TABLE_NAME = "sense";

	public static final String ENTRY_ID = "entry_id";
	public static final String POS      = "pos";
	public static final String FIELD    = "field";
	public static final String MISC     = "misc";
	public static final String INFO     = "info";
	public static final String DIAL     = "dial";
	public static final String GLOSS    = "gloss";

	public static final int INDEX_ENTRY_ID = 1;
	public static final int INDEX_POS      = 2;
	public static final int INDEX_FIELD    = 3;
	public static final int INDEX_MISC     = 4;
	public static final int INDEX_INFO     = 5;
	public static final int INDEX_DIAL     = 6;
	public static final int INDEX_GLOSS    = 7;

	// Queries
	private static final String SQL_CREATE = "CREATE TABLE sense (_id INTEGER PRIMARY KEY AUTOINCREMENT, entry_id INTEGER NOT NULL, pos TEXT, field TEXT, misc TEXT, info TEXT, dial TEXT, gloss TEXT NOT NULL, FOREIGN KEY(entry_id) REFERENCES entry(entry_id));";
	private static final String SQL_CREATE_FTS = "CREATE VIRTUAL TABLE fts_gloss USING fts4 (content='sense', gloss)";

	static final String SQL_INSERT = "INSERT INTO sense (entry_id, pos, field, misc, info, dial, gloss) VALUES (?, ?, ?, ?, ?, ?, ?)";
	static final String SQL_REBUILD = "INSERT INTO fts_gloss(fts_gloss) VALUES('rebuild')";

	private static final String SQL_DROP = "DROP TABLE IF EXISTS sense;";
	private static final String SQL_DROP_FTS = "DROP TABLE IF EXISTS fts_gloss;";

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
