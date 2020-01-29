package fr.frogdevelopment.nihongo.dico.data.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SentencesClient {

    @GET("sentences/search")
    Call<List<Sentence>> search(@Query("lang") String lang,
                                @Query("kanji") String kanji,
                                @Query("kana") String kana,
                                @Query("gloss") String gloss);
}
