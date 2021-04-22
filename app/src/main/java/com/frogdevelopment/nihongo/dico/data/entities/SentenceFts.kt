package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Sense::class)
@Entity(tableName = "fts_sentences")
class SentenceFts(val indices: String)