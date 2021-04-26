package com.frogdevelopment.nihongo.dico.data.details

import androidx.annotation.WorkerThread
import com.frogdevelopment.nihongo.dico.data.entities.Favorite
import com.frogdevelopment.nihongo.dico.data.room.FavoriteDao

class FavoritesRepository(private val favoriteDao: FavoriteDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(senseSeq: String) {
        return favoriteDao.insert(Favorite(0, senseSeq))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(senseSeq: String) {
        return favoriteDao.delete(Favorite(0, senseSeq))
    }
}