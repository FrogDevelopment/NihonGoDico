package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Sense::class)
@Entity(tableName = "fts_gloss")
class SenseFts(val gloss: String)