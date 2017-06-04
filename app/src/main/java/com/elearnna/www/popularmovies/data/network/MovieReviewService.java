package com.elearnna.www.popularmovies.data.network;

import com.elearnna.www.popularmovies.data.model.UserReviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 5/27/2017.
 */

public interface MovieReviewService {
    @GET("movie/{id}/reviews")
    Call<UserReviews> getMovieReviews(@Path("id") int movieId, @Query("api_key") String apiKey);
}
