package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class EntryContract implements BaseColumns {

    public static final String ENTRY_SEQ = "entry_seq";
    public static final String KANJI     = "kanji";
    public static final String KANA      = "kana";
    public static final String READING   = "reading";

    public static final int INDEX_ENTRY_SEQ = 1;
    public static final int INDEX_KANJI     = 2;
    public static final int INDEX_KANA      = 3;
    public static final int INDEX_READING   = 4;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE entries (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, entry_seq TEXT NOT NULL, kanji TEXT, kana TEXT NOT NULL, reading TEXT NOT NULL);";
    private static final String SQL_CREATE_INDEX = "CREATE UNIQUE INDEX index_entries_entry_seq ON entries(entry_seq);";
    private static final String SQL_CREATE_FTS   = "CREATE VIRTUAL TABLE fts_entries USING fts4 (content=`entries`, `kanji`, `kana`)";
    private static final String SQL_CREATE_VIEW  = "CREATE VIEW `EntryDetails` AS SELECT entries.kanji, entries.kana, entries.reading, senses.sense_seq, senses.pos, senses.field, senses.misc, senses.info, senses.dial, senses.gloss, CASE WHEN favorites.rowid IS NULL THEN 0 ELSE 1 END as favorite FROM entries INNER JOIN senses ON senses.entry_seq = entries.entry_seq LEFT OUTER JOIN favorites ON (favorites.sense_seq = senses.sense_seq)";
    private static final String SQL_INSERT       = "INSERT INTO entries (entry_seq, kanji, kana, reading) VALUES (?,?,?,?);";
    private static final String SQL_CLEAN        = "DELETE FROM entries;";
    private static final String SQL_REBUILD      = "INSERT INTO fts_entries(fts_entries) VALUES('rebuild');";

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_INDEX);
        db.execSQL(SQL_CREATE_FTS);
        db.execSQL(SQL_CREATE_VIEW);
    }

    static SQLiteStatement compileInsertStatement(SQLiteDatabase db) {
        return db.compileStatement(SQL_INSERT);
    }

    static void clean(SQLiteDatabase db) {
        db.execSQL(SQL_CLEAN);
    }

    static void reBuild(SQLiteDatabase db) {
        db.execSQL(SQL_REBUILD);
    }

}
