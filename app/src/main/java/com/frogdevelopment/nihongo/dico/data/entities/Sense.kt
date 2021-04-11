package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties("rowid")
@Entity(tableName = "senses")
class Sense(
        @PrimaryKey(autoGenerate = true) val rowid: Int,
        val entry_seq: String,
        val pos: String?,
        val field: String?,
        val misc: String?,
        val info: String?,
        val dial: String?,
        val gloss: String
) : Serializable