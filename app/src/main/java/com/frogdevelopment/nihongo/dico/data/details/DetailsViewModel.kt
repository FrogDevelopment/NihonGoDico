package com.frogdevelopment.nihongo.dico.data.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.rest.Sentence
import com.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val offlineRepository = (application as NihonGoDicoApplication).offlineRepository
    private val onlineRepository = (application as NihonGoDicoApplication).onlineRepository

    private lateinit var entryDetails: LiveData<EntryDetails?>

    fun entryDetails(): LiveData<EntryDetails?> {
        return entryDetails
    }

    fun sentences(entryDetails: EntryDetails): LiveData<List<Sentence>?> {
        return if (isOffline()) {
            MutableLiveData()
        } else {
            onlineRepository.getSentences(entryDetails)
        }
    }

    fun searchEntryDetails(senseSeq: String): LiveData<EntryDetails?> {
        return if (isOffline()) {
            MutableLiveData()
        } else {
            entryDetails = onlineRepository.getEntryDetails(senseSeq)
            entryDetails
        }
    }

    private fun isOffline(): Boolean {
        return preferences.getBoolean(SettingsFragment.KEY_OFFLINE, SettingsFragment.OFFLINE_DEFAULT)
    }

}