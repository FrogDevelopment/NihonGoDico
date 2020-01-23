package fr.frogdevelopment.nihongo.dico.rest;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.search.Entry;
import fr.frogdevelopment.nihongo.dico.search.EntryDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EntriesClient {

    @GET("entries/search")
    Call<List<Entry>> search(@Query("lang") String lang,
                             @Query("query") String query);

    @GET("entries/search/details")
    Call<EntryDetails> getDetails(@Query("lang") String lang,
                                  @Query("senseSeq") String senseSeq);
}
