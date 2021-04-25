package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.frogdevelopment.nihongo.dico.data.entities.Sentence

@Dao
interface SentenceDao {

    @Transaction
    @Query("SELECT * FROM sentences INNER JOIN fts_sentences ON (sentences.rowid = fts_sentences.docid) WHERE fts_sentences MATCH :term")
    fun getSentences(term: String): LiveData<List<Sentence>>

}