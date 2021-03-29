package com.frogdevelopment.nihongo.dico.data.entities

import android.text.SpannableString
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

class EntrySearch(
        val senseSeq: String,
        val kanji: String?,
        val kana: String,
        val vocabulary: String
) : Serializable {

    @JsonIgnore
    var kanjiSpannable: SpannableString? = null
    @JsonIgnore
    var kanaSpannable: SpannableString? = null
    @JsonIgnore
    var vocabularySpannable: SpannableString? = null
}