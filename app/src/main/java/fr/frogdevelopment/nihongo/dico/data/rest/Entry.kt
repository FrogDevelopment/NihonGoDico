package fr.frogdevelopment.nihongo.dico.data.rest

import android.text.SpannableString
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

class Entry : Serializable {

    var senseSeq: String? = null
    var kanji: String? = null
    var kana: String? = null
    var vocabulary: String? = null
    @JsonIgnore
    var kanjiSpannable: SpannableString? = null
    @JsonIgnore
    var kanaSpannable: SpannableString? = null
    @JsonIgnore
    var vocabularySpannable: SpannableString? = null
}