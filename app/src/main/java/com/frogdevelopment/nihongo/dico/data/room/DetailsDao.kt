package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails

@Dao
interface DetailsDao {

    @Query("SELECT * FROM entrydetails WHERE sense_seq = :senseSeq")
    fun getDetails(senseSeq: String): LiveData<EntryDetails>

    @Query("SELECT * FROM entrydetails WHERE favorite = 1")
    fun getFavorites(): LiveData<List<EntryDetails>>

}