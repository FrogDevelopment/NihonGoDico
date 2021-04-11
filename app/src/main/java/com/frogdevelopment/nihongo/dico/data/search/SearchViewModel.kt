package com.frogdevelopment.nihongo.dico.data.search

import android.app.Application
import android.content.SharedPreferences
import android.provider.SearchRecentSuggestions
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.AUTHORITY
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.MODE
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import com.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment
import org.apache.commons.lang3.StringUtils.*

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val searchRecentSuggestions: SearchRecentSuggestions = SearchRecentSuggestions(application.applicationContext, AUTHORITY, MODE)
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val offlineRepository = (application as NihonGoDicoApplication).offlineRepository
    private val onlineRepository = (application as NihonGoDicoApplication).onlineRepository

    private val isSearching: MutableLiveData<Boolean> = MutableLiveData(false)

    fun searching(): LiveData<Boolean> = isSearching

    private val currentQuery: MutableLiveData<String> = MutableLiveData<String>()

    fun entries(): LiveData<List<EntrySearch>> = Transformations.switchMap(currentQuery, this::searchForQuery)

    private fun searchForQuery(query: String): LiveData<List<EntrySearch>> {
        val liveData = when {
            isBlank(query) -> MutableLiveData(emptyList())
            isOffline() -> offlineRepository.search(query)
            else -> onlineRepository.search(query)
        }

        return Transformations.map(liveData) { data ->
            isSearching.value = false

            return@map data
        }
    }

    fun search(query: String) {
        isSearching.value = true
        currentQuery.value = query
    }

    fun isOffline(): Boolean {
        return preferences.getBoolean(SettingsFragment.KEY_OFFLINE, SettingsFragment.OFFLINE_DEFAULT)
    }

    fun saveRecentQuery(secondLine: String) {
        searchRecentSuggestions.saveRecentQuery(currentQuery.value, secondLine)
    }
}
