package com.frogdevelopment.nihongo.dico.data.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import com.frogdevelopment.nihongo.dico.data.entities.SearchResultEntry
import com.frogdevelopment.nihongo.dico.data.room.EntryDao
import com.frogdevelopment.nihongo.dico.utils.Input
import com.frogdevelopment.nihongo.dico.utils.Input.KANA
import com.frogdevelopment.nihongo.dico.utils.Input.KANJI
import java.lang.String.format
import java.util.stream.Collectors

class SearchRepository(private val entryDao: EntryDao) {

    fun search(query: String): LiveData<List<EntrySearch>> {

        val inputType = SearchUtils.getInputType(query)

        val term: String
        val searchResult: LiveData<List<SearchResultEntry>>
        when (inputType) {
            KANJI -> {
                term = format("kanji:%s*", query)
                searchResult = entryDao.searchByKanji(term)
            }
            KANA -> {
                term = format("kana:%s*", query)
                searchResult = entryDao.searchByKana(term)
            }
            else -> {
                term = format("*%s*", query)
                searchResult = entryDao.searchByRomaji(term)
            }
        }

        return Transformations.map(searchResult) { entries ->
            entries.stream()
                    .map { row -> entrySearch(row, query, inputType) }
                    .sorted(Comparator.comparingDouble { e -> e.similarity })
                    .collect(Collectors.toList())
                    .reversed()
        }
    }

    private fun entrySearch(row: SearchResultEntry, query: String, inputType: Input): EntrySearch {
        val entrySearch = EntrySearch(row.sense_seq, row.kanji, row.kana, row.gloss)
        ComputeSimilarity.computeSimilarity(entrySearch, query, inputType)
        return entrySearch
    }
}