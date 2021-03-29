package com.frogdevelopment.nihongo.dico.data.entities

import java.io.Serializable

class EntryDetails(
        val entrySeq: Int,
        val kanji: String?,
        val kana: String,
        val reading: String,
        val pos: Set<String>?,
        val field: Set<String>?,
        val misc: Set<String>?,
        val info: String?,
        val dial: Set<String>?,
        val gloss: String? = null
) : Serializable