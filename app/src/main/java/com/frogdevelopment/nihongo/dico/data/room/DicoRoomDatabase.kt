package com.frogdevelopment.nihongo.dico.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.frogdevelopment.nihongo.dico.data.entities.Entry
import com.frogdevelopment.nihongo.dico.data.entities.Sense

@Database(entities = [Entry::class, Sense::class], version = 1, exportSchema = false)
abstract class DicoRoomDatabase : RoomDatabase() {

    abstract fun entryDao(): EntryDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DicoRoomDatabase? = null

        fun getDatabase(context: Context): DicoRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DicoRoomDatabase::class.java,
                        "NIHON_GO_DICO"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}