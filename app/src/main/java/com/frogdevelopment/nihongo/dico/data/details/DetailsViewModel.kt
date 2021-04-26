package com.frogdevelopment.nihongo.dico.data.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.entities.Sentence
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val detailsRepository = (application as NihonGoDicoApplication).detailsRepository
    private val sentencesRepository = (application as NihonGoDicoApplication).sentencesRepository
    private val favoritesRepository = (application as NihonGoDicoApplication).favoriteRepository

    private lateinit var details: LiveData<EntryDetails>

    fun searchEntryDetails(senseSeq: String): LiveData<EntryDetails> {
        details = detailsRepository.getDetails(senseSeq)
        return details
    }

    fun entryDetails(): LiveData<EntryDetails> {
        return details
    }

    fun sentences(entryDetails: EntryDetails): LiveData<List<Sentence>> {
        return sentencesRepository.getSentences(entryDetails)
    }


    fun toggleFavorite(entryDetails: EntryDetails) = viewModelScope.launch {
        if (entryDetails.favorite) {
            favoritesRepository.delete(entryDetails.senseSeq)
        } else {
            favoritesRepository.insert(entryDetails.senseSeq)
        }
        entryDetails.favorite = !entryDetails.favorite
    }


}