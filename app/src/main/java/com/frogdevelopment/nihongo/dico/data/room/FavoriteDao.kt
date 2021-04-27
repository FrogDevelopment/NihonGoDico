package com.frogdevelopment.nihongo.dico.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frogdevelopment.nihongo.dico.data.entities.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE sense_seq = :senseSeq")
    suspend fun delete(senseSeq: String)

}