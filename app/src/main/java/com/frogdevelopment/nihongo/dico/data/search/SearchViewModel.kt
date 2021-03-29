package com.frogdevelopment.nihongo.dico.data.search

import android.app.Application
import android.provider.SearchRecentSuggestions
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.preference.PreferenceManager
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.OnlineRepository
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.AUTHORITY
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.MODE
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import com.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val searchRecentSuggestions = SearchRecentSuggestions(application.applicationContext, AUTHORITY, MODE)
    private val preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val onlineRepository = OnlineRepository

    var isSearching = MutableLiveData(false)
    private val currentQuery: MutableLiveData<String> = MutableLiveData()
    val entries: LiveData<List<EntrySearch>> = Transformations.switchMap(currentQuery) { query ->
        if (isOffline()) {
            MutableLiveData()
        } else {
           Transformations.map(onlineRepository.search(language(), query)) { entries ->
               isSearching.value = false
               if (entries != null) {
                   searchRecentSuggestions.saveRecentQuery(query, application.applicationContext.resources.getQuantityString(R.plurals.search_results, entries.size, entries.size))
               }
               entries
           }
        }
    }

    fun search(query: String) {
        isSearching.value = true
        currentQuery.value = query
    }

    fun isOffline(): Boolean {
        return preferences.getBoolean(SettingsFragment.KEY_OFFLINE, SettingsFragment.OFFLINE_DEFAULT)
    }

    private fun language(): String {
        return preferences.getString(SettingsFragment.KEY_LANGUAGE, SettingsFragment.LANGUAGE_DEFAULT)!!
    }
}
