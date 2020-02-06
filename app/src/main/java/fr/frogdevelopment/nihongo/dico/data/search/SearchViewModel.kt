package fr.frogdevelopment.nihongo.dico.data.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import fr.frogdevelopment.nihongo.dico.data.OnlineRepository
import fr.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val onlineRepository = OnlineRepository

    private var _query = MutableLiveData<String?>()

    fun query(): MutableLiveData<String?> {
        return _query
    }

    fun search(query: String): MutableLiveData<List<EntrySearch>?> {
        return if (isOffline()) {
            MutableLiveData()
        } else {
            onlineRepository.search(language(), query)
        }
    }

    fun isOffline(): Boolean {
        return preferences.getBoolean(SettingsFragment.KEY_OFFLINE, SettingsFragment.OFFLINE_DEFAULT)
    }

    private fun language(): String {
        return preferences.getString(SettingsFragment.KEY_LANGUAGE, SettingsFragment.LANGUAGE_DEFAULT)!!
    }
}