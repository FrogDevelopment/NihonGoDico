/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ExampleContract implements BaseColumns {

	public static final String TABLE_NAME = "example";

	public static final String JAPANESE    = "japanese";
	public static final String TRANSLATION = "translation";

	public static final int INDEX_ID          = 0;
	public static final int INDEX_JAPANESE    = 1;
	public static final int INDEX_TRANSLATION = 2;

	public static final String[] COLUMNS = {_ID, JAPANESE, TRANSLATION};

	// Queries
	private static final String SQL_CREATE = "CREATE VIRTUAL TABLE example USING FTS4 (japanese,translation);";
	static final         String SQL_INSERT = "INSERT INTO example (japanese, translation) VALUES (?,?)";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS example;";
	static final String SQL_SELECTION = "example MATCH ?";

	static void create(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	static void delete(SQLiteDatabase db) {
		db.execSQL(SQL_DELETE);
	}

}
