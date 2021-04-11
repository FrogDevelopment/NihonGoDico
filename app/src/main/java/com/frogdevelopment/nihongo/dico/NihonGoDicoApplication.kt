package com.frogdevelopment.nihongo.dico

import android.app.Application
import androidx.preference.PreferenceManager
import com.frogdevelopment.nihongo.dico.data.OnlineRepository
import com.frogdevelopment.nihongo.dico.data.room.DicoRoomDatabase
import com.frogdevelopment.nihongo.dico.data.room.OfflineRepository

class NihonGoDicoApplication : Application() {

    private val database by lazy { DicoRoomDatabase.getDatabase(this) }
    val offlineRepository by lazy { OfflineRepository(database.entryDao()) }
    val onlineRepository by lazy { OnlineRepository(PreferenceManager.getDefaultSharedPreferences(applicationContext)) }
}