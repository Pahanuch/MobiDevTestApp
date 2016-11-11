package ua.tykhonovp.mobidev.testapp.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.tykhonovp.mobidev.testapp.model.BookList;

/**
 * Created by Tikho on 28.10.2016.
 */

public interface IServiceEndPoint {
    @GET("volumes")
    Call<BookList> contributors(
        @Query("q") String q,
        @Query("startIndex") int startIndex,
        @Query("maxResults") int maxResults);
    }
