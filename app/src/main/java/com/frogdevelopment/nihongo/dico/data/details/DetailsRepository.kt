package com.frogdevelopment.nihongo.dico.data.details

import androidx.lifecycle.LiveData
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.room.DetailsDao

class DetailsRepository(private val detailsDao: DetailsDao) {

    fun getDetails(senseSeq: String): LiveData<EntryDetails> {
        return detailsDao.getDetails(senseSeq)
    }

    fun getFavorites(): LiveData<List<EntryDetails>> {
        return detailsDao.getFavorites()
    }
}