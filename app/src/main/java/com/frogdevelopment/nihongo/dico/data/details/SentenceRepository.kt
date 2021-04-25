package com.frogdevelopment.nihongo.dico.data.details

import androidx.lifecycle.LiveData
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.entities.Sentence
import com.frogdevelopment.nihongo.dico.data.room.SentenceDao

class SentenceRepository(private val sentenceDao: SentenceDao) {

    fun getSentences(entryDetails: EntryDetails): LiveData<List<Sentence>> {
        return sentenceDao.getSentences("*${entryDetails.kanji}*")
    }
}