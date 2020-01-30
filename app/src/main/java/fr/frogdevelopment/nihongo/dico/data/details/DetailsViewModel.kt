package fr.frogdevelopment.nihongo.dico.data.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import fr.frogdevelopment.nihongo.dico.data.OnlineRepository
import fr.frogdevelopment.nihongo.dico.data.rest.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.rest.Sentence
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val onlineRepository = OnlineRepository.instance

    private val liveData = MutableLiveData<EntryDetails>()

    fun setDetails(entryDetails: EntryDetails) {
        this.liveData.postValue(entryDetails)
    }

    fun entryDetails(): EntryDetails? {
        return liveData.value
    }

    fun sentences(): MutableLiveData<List<Sentence>?> {
        return if (isOffline()) {
            MutableLiveData()
        } else {
            onlineRepository.getSentences(language(), entryDetails()!!)
        }
    }

    private fun isOffline(): Boolean {
        return preferences.getBoolean(SettingsFragment.KEY_OFFLINE, SettingsFragment.OFFLINE_DEFAULT)
    }

    private fun language(): String {
        return preferences.getString(SettingsFragment.KEY_LANGUAGE, SettingsFragment.LANGUAGE_DEFAULT)!!
    }

}