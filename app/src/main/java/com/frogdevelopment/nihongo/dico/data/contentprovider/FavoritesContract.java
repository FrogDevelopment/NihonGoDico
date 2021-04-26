package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class FavoritesContract implements BaseColumns {

    private static final String SQL_CREATE       = "CREATE TABLE favorites (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sense_seq TEXT NOT NULL, FOREIGN KEY(sense_seq) REFERENCES senses(sense_seq) ON DELETE CASCADE)";
    private static final String SQL_CREATE_INDEX = "CREATE UNIQUE INDEX index_favorites_sense_seq ON favorites(sense_seq);";

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_CREATE_INDEX);
    }
}
