package com.frogdevelopment.nihongo.dico.data.rest

import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EntriesClient {

    @GET("nihongo-dico-entries/search/{query}")
    fun search(@Path("query") query: String,
               @Query("lang") lang: String): Call<List<EntrySearch>>

    @GET("nihongo-dico-entries/search/details/{senseSeq}")
    fun getDetails(@Path("senseSeq") senseSeq: String,
                   @Query("lang") lang: String): Call<EntryDetails>

}