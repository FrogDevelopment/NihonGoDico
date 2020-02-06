package fr.frogdevelopment.nihongo.dico.data.rest

import fr.frogdevelopment.nihongo.dico.data.entities.Entry
import fr.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EntriesClient {

    @GET("entries/search")
    fun search(@Query("lang") lang: String,
               @Query("query") query: String): Call<List<EntrySearch>>

    @GET("entries/search/details")
    fun getDetails(@Query("lang") lang: String,
                   @Query("senseSeq") senseSeq: String): Call<EntryDetails>

    @GET("entries/export")
    fun import(@Query("lang") lang: String): Call<List<Entry>>
}