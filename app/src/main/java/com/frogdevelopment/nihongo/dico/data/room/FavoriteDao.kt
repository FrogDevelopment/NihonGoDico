package com.frogdevelopment.nihongo.dico.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.frogdevelopment.nihongo.dico.data.entities.Favorite
import com.frogdevelopment.nihongo.dico.data.entities.SearchResultEntry

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT senses.sense_seq, entries.kanji, entries.kana, senses.gloss, 1 as favorite" +
            " FROM entries" +
            " INNER JOIN senses ON (entries.entry_seq = senses.entry_seq)" +
            " INNER JOIN favorites ON (senses.sense_seq = favorites.sense_seq)")
    fun getAll(): LiveData<List<SearchResultEntry>>

}