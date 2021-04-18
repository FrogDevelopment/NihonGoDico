package com.frogdevelopment.nihongo.dico.data.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.entities.Sentence

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val offlineRepository = (application as NihonGoDicoApplication).searchRepository

    private lateinit var entryDetails: LiveData<EntryDetails?>

    fun entryDetails(): LiveData<EntryDetails?> {
        return entryDetails
    }

    fun sentences(entryDetails: EntryDetails): LiveData<List<Sentence>?> {
        return MutableLiveData()
//            return offlineRepository.getSentences(entryDetails)
    }

    fun searchEntryDetails(senseSeq: String): LiveData<EntryDetails?> {
        return MutableLiveData()
//        entryDetails = onlineRepository.getEntryDetails(senseSeq)
//        return entryDetails
    }

}