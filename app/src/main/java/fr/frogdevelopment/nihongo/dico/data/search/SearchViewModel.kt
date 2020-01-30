package fr.frogdevelopment.nihongo.dico.data.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import fr.frogdevelopment.nihongo.dico.data.OnlineRepository
import fr.frogdevelopment.nihongo.dico.data.rest.Entry

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val onlineRepository = OnlineRepository

    private val entries = MutableLiveData<List<Entry>>()
    private val searching = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()

    fun setEntries(entries: List<Entry>) {
        this.entries.postValue(entries)
    }

    fun entries(): MutableLiveData<List<Entry>> {
        return entries
    }

    fun isSearching(isSearching: Boolean) {
        searching.postValue(isSearching)
    }

    fun searching(): MutableLiveData<Boolean> {
        return searching
    }

    fun setError(error: String) {
        this.error.postValue(error)
    }

    fun error(): MutableLiveData<String> {
        return error
    }
}