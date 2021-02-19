package fr.frogdevelopment.nihongo.dico.data.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SentencesClient {

    @GET("nihongo-dico-sentences/search")
    fun search(@Query("lang") lang: String,
               @Query("kanji") kanji: String?,
               @Query("kana") kana: String,
               @Query("gloss") gloss: String): Call<List<Sentence>?>
}