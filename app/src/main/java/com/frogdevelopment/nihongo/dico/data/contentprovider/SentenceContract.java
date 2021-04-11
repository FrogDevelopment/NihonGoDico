package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

// cf http://stackoverflow.com/a/29926430/244911
public class SentenceContract implements BaseColumns {

    // line format : Jpn_seq_no[TAB]Eng_seq_no[TAB]Japanese sentence[TAB]Translation sentence[TAB]Indices
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
    private static final String SQL_CREATE = "CREATE TABLE sentences (rowid INTEGER PRIMARY KEY NOT NULL, japanese_ref INTEGER,translation_ref INTEGER,japanese_sentence TEXT NOT NULL,translation_sentence TEXT NOT NULL,indices TEXT NOT NULL);";
    private static final String SQL_INSERT = "INSERT INTO sentences (japanese_ref,translation_ref,japanese_sentence,translation_sentence,indices) VALUES (?,?,?,?,?);";
    private static final String SQL_CLEAN  = "DELETE FROM sentences;";

    static SQLiteStatement compileInsertStatement(SQLiteDatabase db) {
        return db.compileStatement(SQL_INSERT);
    }

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    static void clean(SQLiteDatabase db) {
        db.execSQL(SQL_CLEAN);
    }
}
