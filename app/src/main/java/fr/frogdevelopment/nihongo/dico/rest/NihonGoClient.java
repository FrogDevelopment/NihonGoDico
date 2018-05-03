package fr.frogdevelopment.nihongo.dico.rest;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.search.Search;
import fr.frogdevelopment.nihongo.dico.search.SearchDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NihonGoClient {

    @GET("/api/search")
    Call<List<Search>> search(@Query("lang") String lang,
                              @Query("query") String query);

    @GET("/api/details")
    Call<SearchDetails> getDetails(@Query("lang") String lang,
                             @Query("senseId") Long senseId);
}
