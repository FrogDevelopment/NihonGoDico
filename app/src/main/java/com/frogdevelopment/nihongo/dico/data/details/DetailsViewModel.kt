package com.frogdevelopment.nihongo.dico.data.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.frogdevelopment.nihongo.dico.data.OnlineRepository
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.rest.Sentence
import com.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val onlineRepository = OnlineRepository

    private lateinit var entryDetails: MutableLiveData<EntryDetails?>

    fun entryDetails(): MutableLiveData<EntryDetails?> {
        return entryDetails
    }

    fun sentences(entryDetails: EntryDetails): MutableLiveData<List<Sentence>?> {
        return if (isOffline()) {
            MutableLiveData()
        } else {
            onlineRepository.getSentences(language(), entryDetails)
        }
    }

    fun searchEntryDetails(senseSeq: String): MutableLiveData<EntryDetails?> {
        return if (isOffline()) {
            MutableLiveData()
        } else {
            entryDetails = onlineRepository.getEntryDetails(language(), senseSeq)
            entryDetails
        }
    }

    private fun isOffline(): Boolean {
        return preferences.getBoolean(SettingsFragment.KEY_OFFLINE, SettingsFragment.OFFLINE_DEFAULT)
    }

    private fun language(): String {
        return preferences.getString(SettingsFragment.KEY_LANGUAGE, SettingsFragment.LANGUAGE_DEFAULT)!!
    }

}