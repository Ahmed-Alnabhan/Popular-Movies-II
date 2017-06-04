package com.elearnna.www.popularmovies.data.network;

import com.elearnna.www.popularmovies.data.model.MovieTrailersResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 5/25/2017.
 */

public interface MovieTrailerService {
    @GET("movie/{id}/videos")
    Call <MovieTrailersResult> getMovieTrailers(@Path("id") int movieId, @Query("api_key") String apiKey);
}
