package com.frogdevelopment.nihongo.dico.data.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.frogdevelopment.nihongo.dico.NihonGoDicoApplication
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val detailsRepository = (application as NihonGoDicoApplication).detailsRepository

    fun getFavorites(): LiveData<List<EntryDetails>> {
        return detailsRepository.getFavorites()
    }

}
