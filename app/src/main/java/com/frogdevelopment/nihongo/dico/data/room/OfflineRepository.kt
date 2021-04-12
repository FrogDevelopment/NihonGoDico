package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import java.util.stream.Collectors

class OfflineRepository(private val entryDao: EntryDao) {

    fun search(query: String): LiveData<List<EntrySearch>> {
        val wildcardQuery = java.lang.String.format("*%s*", query)
        return Transformations.map(entryDao.searchByEnglishTerm(wildcardQuery)) { entries ->
            entries.stream()
                    .map { e -> EntrySearch(e.sense_seq, e.kanji, e.kana, e.gloss) }
                    .collect(Collectors.toList())
        }

    }
}