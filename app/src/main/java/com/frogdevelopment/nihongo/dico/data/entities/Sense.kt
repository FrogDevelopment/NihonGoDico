package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "senses", indices = [Index(value = ["entry_seq"]), Index(value = ["sense_seq"], unique = true)])
class Sense(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        @ColumnInfo(name = "entry_seq") val entrySeq: String,
        @ColumnInfo(name = "sense_seq") val senseSeq: String,
        @ColumnInfo val pos: String?,
        @ColumnInfo val field: String?,
        @ColumnInfo val misc: String?,
        @ColumnInfo val info: String?,
        @ColumnInfo val dial: String?,
        @ColumnInfo val gloss: String
)