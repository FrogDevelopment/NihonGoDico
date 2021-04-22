package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails

@Dao
interface SenseDao {

    @Query("SELECT * FROM entrydetails WHERE sense_seq = :senseSeq")
    fun getDetails(senseSeq: String): LiveData<EntryDetails>

}