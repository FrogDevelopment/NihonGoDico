package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class FavoritesContract implements BaseColumns {

    private static final String SQL_CREATE = "CREATE TABLE favorites (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sense_seq TEXT NOT NULL, FOREIGN KEY(sense_seq) REFERENCES senses(sense_seq))";

    static final String SQL_INSERT = "INSERT INTO favorites (sense_seq) VALUES (?)";

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }
}
