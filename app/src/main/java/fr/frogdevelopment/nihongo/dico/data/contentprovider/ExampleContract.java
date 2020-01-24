/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

// cf http://stackoverflow.com/a/29926430/244911
public class ExampleContract implements BaseColumns {

    // line format : Jpn_seq_no[TAB]Eng_seq_no[TAB]Japanese sentence[TAB]Translation sentence[TAB]Indices
    // indices : cf http://www.edrdg.org/wiki/index.php/Sentence-Dictionary_Linking
    // order : JMDict_word(reading)[sense_number]{form_in_sentence}~

    public static final String JAPANESE_REF = "japanese_ref";
    public static final String TRANSLATION_REF = "translation_ref";
    public static final String JAPANESE_SENTENCE = "japanese_sentence";
    public static final String TRANSLATION_SENTENCE = "translation_sentence";
    public static final String INDICES = "indices";

    public static final int INDEX_JAPANESE_REF = 1;
    public static final int INDEX_TRANSLATION_REF = 2;
    public static final int INDEX_JAPANESE_SENTENCE = 3;
    public static final int INDEX_TRANSLATION_SENTENCE = 4;
    public static final int INDEX_INDICES = 5;

    // Queries
    private static final String SQL_CREATE = "CREATE TABLE example (_id INTEGER PRIMARY KEY, japanese_ref INTEGER,translation_ref INTEGER,japanese_sentence TEXT NOT NULL,translation_sentence TEXT NOT NULL,indices TEXT NOT NULL);";
    private static final String SQL_CREATE_FTS = "CREATE VIRTUAL TABLE fts_example USING fts4 (content='example', indices)";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS example;";
    private static final String SQL_DROP_FTS = "DROP TABLE IF EXISTS fts_example;";

    static final String SQL_INSERT = "INSERT INTO example (japanese_ref,translation_ref,japanese_sentence,translation_sentence,indices) VALUES (?,?,?,?,?)";
    static final String SQL_REBUILD = "INSERT INTO fts_example(fts_example) VALUES('rebuild')";

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_CREATE_FTS);
    }

    static void drop(SQLiteDatabase db) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_DROP_FTS);
    }

}
