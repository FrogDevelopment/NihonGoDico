package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "entries", indices = [Index(value = ["entry_seq"], unique = true)])
class Entry(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        @ColumnInfo(name = "entry_seq") val entrySeq: String,
        @ColumnInfo val kanji: String?,
        @ColumnInfo val kana: String,
        @ColumnInfo val reading: String) : Serializable
