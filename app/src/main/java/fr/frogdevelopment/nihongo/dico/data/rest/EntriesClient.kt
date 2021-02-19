package fr.frogdevelopment.nihongo.dico.data.rest

import fr.frogdevelopment.nihongo.dico.data.entities.Entry
import fr.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.entities.EntrySearch
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

    @GET("nihongo-dico-entries/export")
    fun import(@Query("lang") lang: String): Call<List<Entry>>
}