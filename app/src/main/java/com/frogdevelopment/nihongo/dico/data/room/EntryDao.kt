package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.frogdevelopment.nihongo.dico.data.entities.Entry

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries e WHERE e.rowid < 500 ORDER BY entry_seq ASC")
    fun getEntries(): LiveData<List<Entry>>
}