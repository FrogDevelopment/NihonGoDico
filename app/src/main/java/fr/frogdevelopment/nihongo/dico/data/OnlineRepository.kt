package fr.frogdevelopment.nihongo.dico.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import fr.frogdevelopment.nihongo.dico.data.rest.Entry
import fr.frogdevelopment.nihongo.dico.data.rest.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory
import fr.frogdevelopment.nihongo.dico.data.rest.Sentence
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object OnlineRepository {

    private val entriesClient = RestServiceFactory.entriesClient
    private val sentencesClient = RestServiceFactory.sentencesClient

    fun search(language: String, query: String): MutableLiveData<List<Entry>?> {
        val entries = MutableLiveData<List<Entry>?>()
        entriesClient
                .search(language, query)
                .enqueue(object : Callback<List<Entry>> {
                    override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                        if (response.isSuccessful) {
                            entries.postValue(response.body())
                        } else {
                            Log.e("NIHONGO_DICO", "Fetching entries response code : " + response.code())
                            entries.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                        Log.e("NIHONGO_DICO", "Fetching entries error", t)
                        entries.postValue(null)
                    }
                })

        return entries
    }

    fun getEntryDetails(language: String, senseSeq: String): MutableLiveData<EntryDetails?> {
        val entryDetails = MutableLiveData<EntryDetails?>()
        entriesClient
                .getDetails(language, senseSeq)
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

    fun getSentences(lang: String?, entryDetails: EntryDetails): MutableLiveData<List<Sentence>?> {
        val sentences = MutableLiveData<List<Sentence>?>()
        sentencesClient
                .search(lang!!, entryDetails.kanji, entryDetails.kana!!, entryDetails.gloss!!)
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