package com.frogdevelopment.nihongo.dico

import android.app.Application
import com.frogdevelopment.nihongo.dico.data.room.DicoRoomDatabase
import com.frogdevelopment.nihongo.dico.data.search.DetailsRepository
import com.frogdevelopment.nihongo.dico.data.search.SearchRepository

class NihonGoDicoApplication : Application() {

    private val database by lazy { DicoRoomDatabase.getDatabase(this) }
    val searchRepository by lazy { SearchRepository(database.entryDao()) }
    val detailsRepository by lazy { DetailsRepository(database.senseDao()) }
}