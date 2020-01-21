package fr.frogdevelopment.nihongo.dico.rest;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.search.Entry;
import fr.frogdevelopment.nihongo.dico.search.SearchDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NihonGoClient {

    @GET("api/nihongo/dico/entries/search")
    Call<List<Entry>> search(@Query("lang") String lang,
                             @Query("query") String query);

    @GET("api/details")
    Call<SearchDetails> getDetails(@Query("lang") String lang,
                                   @Query("senseId") Long senseId);
}
