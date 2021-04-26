package com.frogdevelopment.nihongo.dico.data.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT entries.kanji, entries.kana, entries.reading, senses.sense_seq, senses.pos, senses.field, senses.misc, senses.info, senses.dial, senses.gloss, CASE WHEN favorites.rowid IS NULL THEN 0 ELSE 1 END as favorite" +
        " FROM entries" +
        " INNER JOIN senses ON senses.entry_seq = entries.entry_seq" +
        " LEFT OUTER JOIN favorites ON (favorites.sense_seq = senses.sense_seq)")
class EntryDetails(
        val kanji: String?,
        val kana: String,
        val reading: String,
        @ColumnInfo(name = "sense_seq") val senseSeq: String,
        val pos: String?,
        val field: String?,
        val misc: String?,
        val info: String?,
        val dial: String?,
        val gloss: String? = null,
        var favorite: Boolean
)