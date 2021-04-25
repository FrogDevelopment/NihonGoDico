package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentences")
class Sentence(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        @ColumnInfo(name = "japanese") val japanese: String,
        @ColumnInfo(name = "translation") val translation: String,
        @ColumnInfo(name = "linking") val linking: String)