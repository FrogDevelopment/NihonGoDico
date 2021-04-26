package com.frogdevelopment.nihongo.dico.data.entities

class SearchResultEntry(
        val sense_seq: String,
        val kanji: String?,
        val kana: String,
        val gloss: String,
        val favorite: Boolean
)