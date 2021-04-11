package com.frogdevelopment.nihongo.dico.data

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import com.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory
import com.frogdevelopment.nihongo.dico.data.rest.Sentence
import com.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnlineRepository(private val preferences: SharedPreferences) {

    private val entriesClient = RestServiceFactory.entriesClient
    private val sentencesClient = RestServiceFactory.sentencesClient

    private fun language(): String {
        return preferences.getString(SettingsFragment.KEY_LANGUAGE, SettingsFragment.LANGUAGE_DEFAULT)!!
    }

    fun search(query: String): MutableLiveData<List<EntrySearch>> {
        val entries = MutableLiveData<List<EntrySearch>>()
        entriesClient
                .search(query, language())
                .enqueue(object : Callback<List<EntrySearch>> {
                    override fun onResponse(call: Call<List<EntrySearch>>, response: Response<List<EntrySearch>>) {
                        if (response.isSuccessful) {
                            entries.postValue(response.body())
                        } else {
                            Log.e("NIHONGO_DICO", "Fetching entries response code : " + response.code())
                            entries.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<List<EntrySearch>>, t: Throwable) {
                        Log.e("NIHONGO_DICO", "Fetching entries error", t)
                        entries.postValue(null)
                    }
                })

        return entries
    }

    fun getEntryDetails(senseSeq: String): MutableLiveData<EntryDetails?> {
        val entryDetails = MutableLiveData<EntryDetails?>()
        entriesClient
                .getDetails(senseSeq, language())
                .enqueue(object : Callback<EntryDetails?> {
                    override fun onResponse(call: Call<EntryDetails?>, response: Response<EntryDetails?>) {
                        if (response.isSuccessful) {
                            entryDetails.postValue(response.body())
                        } else {
                            Log.e("NIHONGO_DICO", "Fetching details response code : " + response.code())
                            entryDetails.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<EntryDetails?>, t: Throwable) {
                        Log.e("NIHONGO_DICO", "Fetching details error", t)
                        entryDetails.postValue(null)
                    }
                })

        return entryDetails
    }

    fun getSentences(entryDetails: EntryDetails): MutableLiveData<List<Sentence>?> {
        val sentences = MutableLiveData<List<Sentence>?>()
        sentencesClient
                .search(language(), entryDetails.kanji, entryDetails.kana, entryDetails.gloss!!)
                .enqueue(object : Callback<List<Sentence>?> {
                    override fun onResponse(call: Call<List<Sentence>?>, response: Response<List<Sentence>?>) {
                        if (response.isSuccessful) {
                            sentences.postValue(response.body())
                        } else {
                            Log.e("NIHONGO_DICO", "Fetching sentences response code : " + response.code())
                            sentences.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<List<Sentence>?>, t: Throwable) {
                        Log.e("NIHONGO_DICO", "Fetching sentences error", t)
                        sentences.postValue(null)
                    }
                })
        return sentences
    }
}