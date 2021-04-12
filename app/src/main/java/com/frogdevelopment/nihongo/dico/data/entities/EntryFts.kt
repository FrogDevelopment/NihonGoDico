package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Entry::class)
@Entity(tableName = "fts_entries")
class EntryFts(val kanji: String?, val kana: String)
