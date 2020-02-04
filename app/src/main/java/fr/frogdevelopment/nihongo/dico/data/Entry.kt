package fr.frogdevelopment.nihongo.dico.data

import android.text.SpannableString
import androidx.room.*
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable


@Fts4(notIndexed = ["senseSeq"])
@Entity(tableName = "entries")
class Entry : Serializable {

    @JsonIgnore
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int? = null

    @PrimaryKey(autoGenerate = false)
    var senseSeq: String? = null

    var kanji: String? = null

    var kana: String? = null

    var vocabulary: String? = null

    @Ignore
    @JsonIgnore
    var kanjiSpannable: SpannableString? = null

    @Ignore
    @JsonIgnore
    var kanaSpannable: SpannableString? = null

    @Ignore
    @JsonIgnore
    var vocabularySpannable: SpannableString? = null
}