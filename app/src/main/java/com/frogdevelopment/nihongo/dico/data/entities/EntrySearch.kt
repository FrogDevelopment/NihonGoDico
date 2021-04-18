package com.frogdevelopment.nihongo.dico.data.entities

import android.text.SpannableString

class EntrySearch(
        val senseSeq: String,
        val kanji: String?,
        val kana: String,
        val vocabulary: String
) {

    var kanjiSpannable: SpannableString? = null
    var kanaSpannable: SpannableString? = null
    var vocabularySpannable: SpannableString? = null
    var similarity = 0.0
}