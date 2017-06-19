package fr.frogdevelopment.nihongo.dico.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class FavoritesContract implements BaseColumns {

	static final String TABLE_NAME = "favorites";

	public static final String SENSE_ID = "sense_id";

	private static final String SQL_CREATE = "CREATE TABLE favorites (_id INTEGER PRIMARY KEY AUTOINCREMENT, sense_id INTEGER NOT NULL, FOREIGN KEY(sense_id) REFERENCES sense(sense_id))";

	static final String SQL_INSERT = "INSERT INTO favorites (sense_id) VALUES (?)";

	private static final String SQL_DROP = "DROP TABLE IF EXISTS favorites;";

	static void create(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	static void drop(SQLiteDatabase db) {
		db.execSQL(SQL_DROP);
	}
}
