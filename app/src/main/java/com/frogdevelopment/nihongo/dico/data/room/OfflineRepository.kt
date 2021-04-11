package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import java.util.stream.Collectors

class OfflineRepository(private val entryDao: EntryDao) {

    fun search(query: String): LiveData<List<EntrySearch>> {
        return Transformations.map(entryDao.getEntries()) { entries ->
            entries.stream()
                    .map { entry -> EntrySearch("sense_seq", entry.kanji, entry.kana, "fixme") }
                    .collect(Collectors.toList())
        }
    }
}