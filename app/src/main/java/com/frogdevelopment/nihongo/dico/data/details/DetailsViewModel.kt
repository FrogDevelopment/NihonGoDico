package com.frogdevelopment.nihongo.dico.data.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val detailsRepository = (application as NihonGoDicoApplication).detailsRepository

    private lateinit var details: LiveData<EntryDetails>

    fun searchEntryDetails(senseSeq: String): LiveData<EntryDetails> {
        details = detailsRepository.getDetails(senseSeq)
        return details
    }

    fun entryDetails(): LiveData<EntryDetails> {
        return details
    }

//    fun sentences(entryDetails: EntryDetails): LiveData<List<Sentence>> {
//        return searchRepository.getSentences(entryDetails)
//    }

}