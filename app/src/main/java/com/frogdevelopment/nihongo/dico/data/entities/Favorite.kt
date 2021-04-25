package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class Favorite(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        @ColumnInfo(name = "entry_seq") val entrySeq: String)
