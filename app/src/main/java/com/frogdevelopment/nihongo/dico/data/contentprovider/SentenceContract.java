package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class SentenceContract implements BaseColumns {

    // indices : cf http://www.edrdg.org/wiki/index.php/Sentence-Dictionary_Linking
    // order : JMDict_word(reading)[sense_number]{form_in_sentence}~

    public static final String JAPANESE    = "japanese";
    public static final String TRANSLATION = "translation";
    public static final String LINKING     = "linking";

    public static final int INDEX_JAPANESE    = 1;
    public static final int INDEX_TRANSLATION = 2;
    public static final int INDEX_LINKING     = 3;

    // Queries
    private static final String SQL_CREATE_TABLE = "CREATE TABLE sentences (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, japanese TEXT NOT NULL, translation TEXT NOT NULL, linking TEXT NOT NULL);";
    private static final String SQL_CREATE_FTS   = "CREATE VIRTUAL TABLE fts_sentences USING fts4 (content=`sentences`, linking)";
    private static final String SQL_INSERT       = "INSERT INTO sentences (japanese, translation, linking) VALUES (?,?,?);";
    private static final String SQL_CLEAN        = "DELETE FROM sentences;";
    private static final String SQL_REBUILD      = "INSERT INTO fts_sentences(fts_sentences) VALUES('rebuild')";

    static SQLiteStatement compileInsertStatement(SQLiteDatabase db) {
        return db.compileStatement(SQL_INSERT);
    }

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_FTS);
    }

    static void clean(SQLiteDatabase db) {
        db.execSQL(SQL_CLEAN);
    }

    static void reBuild(SQLiteDatabase db) {
        db.execSQL(SQL_REBUILD);
    }
}
