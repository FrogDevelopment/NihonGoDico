package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentences")
class Sentence(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        @ColumnInfo(name = "japanese_sentence") val japanese: String,
        @ColumnInfo(name = "translation_sentence") val translation: String)