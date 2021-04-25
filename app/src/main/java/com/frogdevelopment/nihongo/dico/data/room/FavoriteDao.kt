package com.frogdevelopment.nihongo.dico.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.frogdevelopment.nihongo.dico.data.entities.Favorite

@Dao
interface FavoriteDao {

    @Insert
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

//    @Query("SELECT senses.sense_seq, entries.kanji, entries.kana, senses.gloss" +
//            " FROM entries" +
//            " INNER JOIN senses ON (entries.entry_seq = senses.entry_seq)" +
//            " INNER JOIN favorites ON (senses.sense_seq = favorites.sense_seq)")
//    fun getAll(): LiveData<List<SearchResultEntry>>

}