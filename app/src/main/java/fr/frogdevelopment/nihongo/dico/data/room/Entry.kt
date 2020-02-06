package fr.frogdevelopment.nihongo.dico.data.room

import android.text.SpannableString
import androidx.room.*
import java.io.Serializable

@Fts4(notIndexed = ["sense_seq"])
@Entity(tableName = "entries")
class Entry(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val id: Int,
            @ColumnInfo(name = "sense_seq") val senseSeq: String,
            val kanji: String,
            val kana: String,
            val vocabulary: String) : Serializable {

    @Ignore
    var kanjiSpannable: SpannableString? = null

    @Ignore
    var kanaSpannable: SpannableString? = null

    @Ignore
    var vocabularySpannable: SpannableString? = null
}