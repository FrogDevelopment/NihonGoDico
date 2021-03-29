package com.frogdevelopment.nihongo.dico.data.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class SenseContract implements BaseColumns {

    public static final String ENTRY_SEQ = "entry_seq";
    //	public static final String SENSE_SEQ = "sense_seq";
    public static final String POS       = "pos";
    public static final String FIELD     = "field";
    public static final String MISC      = "misc";
    public static final String INFO      = "info";
    public static final String DIAL      = "dial";
    public static final String GLOSS     = "gloss";

    public static final int INDEX_ENTRY_SEQ = 1;
    public static final int INDEX_POS       = 2;
    public static final int INDEX_FIELD     = 3;
    public static final int INDEX_MISC      = 4;
    public static final int INDEX_INFO      = 5;
    public static final int INDEX_DIAL      = 6;
    public static final int INDEX_GLOSS     = 7;

    private static final String SQL_CREATE     = "CREATE TABLE senses (_id INTEGER PRIMARY KEY AUTOINCREMENT, entry_seq TEXT NOT NULL, pos TEXT, field TEXT, misc TEXT, info TEXT, dial TEXT, gloss TEXT NOT NULL);";
    private static final String SQL_CREATE_FTS = "CREATE VIRTUAL TABLE fts_gloss USING fts4 (content='senses', gloss)";
    private static final String SQL_INSERT     = "INSERT INTO senses (entry_seq, pos, field, misc, info, dial, gloss) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_CLEAN      = "DELETE FROM senses;";
    private static final String SQL_REBUILD    = "INSERT INTO fts_gloss(fts_gloss) VALUES('rebuild');";

    static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_CREATE_FTS);
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
