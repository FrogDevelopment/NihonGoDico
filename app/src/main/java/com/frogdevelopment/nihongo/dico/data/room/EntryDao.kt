package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.frogdevelopment.nihongo.dico.data.entities.Entry
import com.frogdevelopment.nihongo.dico.data.entities.SearchResultEntry

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries e WHERE e.rowid < 500 ORDER BY entry_seq ASC")
    fun getEntries(): LiveData<List<Entry>>

//    @Transaction
//    @Query("SELECT entries.kanji, entries.kana FROM entries JOIN fts_entries ON (entries.rowid = fts_entries.docid) WHERE fts_entries MATCH :term")
//    fun searchByJapaneseTerm(term: String?): LiveData<List<SearchResultEntry>>

    @Transaction
    @Query("SELECT senses.sense_seq, entries.kanji, entries.kana, snippet(fts_gloss, '<span class=\"keyword\">', '</span>') as gloss" +
            " FROM entries" +
            " INNER JOIN senses ON (entries.entry_seq = senses.entry_seq)" +
            " INNER JOIN fts_gloss ON (senses.rowid = fts_gloss.docid)" +
            " WHERE fts_gloss MATCH :term")
    fun searchByEnglishTerm(term: String): LiveData<List<SearchResultEntry>>

}