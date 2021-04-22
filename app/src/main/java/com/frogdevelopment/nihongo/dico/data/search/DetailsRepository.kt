package com.frogdevelopment.nihongo.dico.data.search

import androidx.lifecycle.LiveData
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.room.SenseDao

class DetailsRepository(private val senseDao: SenseDao) {

    fun getDetails(senseSeq: String): LiveData<EntryDetails> {
        return senseDao.getDetails(senseSeq)
    }
}