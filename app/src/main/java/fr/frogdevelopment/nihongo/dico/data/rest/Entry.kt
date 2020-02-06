package fr.frogdevelopment.nihongo.dico.data.rest

import android.text.SpannableString
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

class Entry(
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