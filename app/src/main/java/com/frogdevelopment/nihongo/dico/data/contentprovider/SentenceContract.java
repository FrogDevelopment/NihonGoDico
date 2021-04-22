package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class SentenceContract implements BaseColumns {

    // indices : cf http://www.edrdg.org/wiki/index.php/Sentence-Dictionary_Linking
    // order : JMDict_word(reading)[sense_number]{form_in_sentence}~

    public static final String JAPANESE_REF         = "japanese_ref";
    public static final String TRANSLATION_REF      = "translation_ref";
    public static final String JAPANESE_SENTENCE    = "japanese_sentence";
    public static final String TRANSLATION_SENTENCE = "translation_sentence";
    public static final String INDICES              = "indices";

    public static final int INDEX_JAPANESE_REF         = 1;
    public static final int INDEX_TRANSLATION_REF      = 2;
    public static final int INDEX_JAPANESE_SENTENCE    = 3;
    public static final int INDEX_TRANSLATION_SENTENCE = 4;
    public static final int INDEX_INDICES              = 5;

    // Queries
    private static final String SQL_CREATE_TABLE = "CREATE TABLE sentences (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, japanese_ref INTEGER,translation_ref INTEGER,japanese_sentence TEXT NOT NULL,translation_sentence TEXT NOT NULL,indices TEXT NOT NULL);";
    private static final String SQL_CREATE_FTS   = "CREATE VIRTUAL TABLE fts_sentences USING fts4 (content='sentences', indices)";
    private static final String SQL_INSERT       = "INSERT INTO sentences (japanese_ref,translation_ref,japanese_sentence,translation_sentence,indices) VALUES (?,?,?,?,?);";
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
