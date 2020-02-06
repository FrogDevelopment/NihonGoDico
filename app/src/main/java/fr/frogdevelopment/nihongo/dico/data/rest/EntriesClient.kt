package fr.frogdevelopment.nihongo.dico.data.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EntriesClient {

    @GET("entries/search")
    fun search(@Query("lang") lang: String,
               @Query("query") query: String): Call<List<Entry>>

    @GET("entries/search/details")
    fun getDetails(@Query("lang") lang: String,
                   @Query("senseSeq") senseSeq: String): Call<EntryDetails>
}