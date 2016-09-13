/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ExampleContract implements BaseColumns {

	public static final String TABLE_NAME = "EXAMPLE";

	public static final String REF      = "REF";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String SENTENCE = "SENTENCE";

	public static final int INDEX_ID       = 0;
	public static final int INDEX_REF      = 1;
	public static final int INDEX_LANGUAGE = 2;
	public static final int INDEX_SENTENCE = 3;

	public static final String[] COLUMNS = {_ID, REF, LANGUAGE, SENTENCE};

	// Queries
	private static final String SQL_CREATE     = "CREATE TABLE EXAMPLE (_ID INTEGER PRIMARY KEY AUTOINCREMENT, REF TEXT NOT NULL, LANGUAGE TEXT NOT NULL, SENTENCE TEXT NOT NULL);";
	private static final String SQL_CREATE_FTS = "CREATE VIRTUAL TABLE FTS_EXAMPLE USING FTS4 (content='EXAMPLE','SENTENCE');";

	static final String SQL_INSERT      = "INSERT INTO EXAMPLE (REF,LANGUAGE,SENTENCE) VALUES (?,?,?)";
	static final String SQL_REBUILD_FTS = "INSERT INTO FTS_EXAMPLE (FTS_EXAMPLE) VALUES ('rebuild');";

	private static final String SQL_DELETE = "DROP TABLE IF EXISTS EXAMPLE;";
	private static final String SQL_DELETE_FTS = "DROP TABLE IF EXISTS FTS_EXAMPLE;";

	static void create(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
		db.execSQL(SQL_CREATE_FTS);
	}

	static void delete(SQLiteDatabase db) {
		db.execSQL(SQL_DELETE);
	}

}
