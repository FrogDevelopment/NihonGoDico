package com.frogdevelopment.nihongo.dico.data.search

import android.app.Application
import android.provider.SearchRecentSuggestions
import androidx.lifecycle.*
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.AUTHORITY
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.MODE
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import org.apache.commons.lang3.StringUtils.*

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val searchRecentSuggestions: SearchRecentSuggestions = SearchRecentSuggestions(application.applicationContext, AUTHORITY, MODE)
    private val searchRepository = (application as NihonGoDicoApplication).searchRepository

    private val isSearching: MutableLiveData<Boolean> = MutableLiveData(false)

    fun searching(): LiveData<Boolean> = isSearching

    private val currentQuery: MutableLiveData<String> = MutableLiveData<String>()

    fun entries(): LiveData<List<EntrySearch>> = Transformations.switchMap(currentQuery, this::searchForQuery)

    private fun searchForQuery(query: String): LiveData<List<EntrySearch>> {
        val liveData = when {
            isBlank(query) -> MutableLiveData(emptyList())
            else -> searchRepository.search(query)
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

    fun saveRecentQuery(secondLine: String) {
        searchRecentSuggestions.saveRecentQuery(currentQuery.value, secondLine)
    }
}
