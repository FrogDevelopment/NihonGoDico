package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties("rowid")
@Entity(tableName = "entries")
@Fts4(notIndexed = ["sense_seq", "pos", "field", "misc", "info", "dial"])
class Entry(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val id: Int,
        @ColumnInfo(name = "entry_seq") val entry_seq: String,
        val kanji: String?,
        val kana: String,
        val reading: String,
        val sense_seq: String,
        val pos: String?,
        val field: String?,
        val misc: String?,
        val info: String?,
        val dial: String?,
        val vocabularies: Array<String>
) : Serializable