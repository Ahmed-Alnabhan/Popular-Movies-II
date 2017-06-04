package com.elearnna.www.popularmovies.utilities;

import com.elearnna.www.popularmovies.data.network.MovieReviewService;
import com.elearnna.www.popularmovies.data.network.MovieTrailerService;
import com.elearnna.www.popularmovies.data.network.RetrofitClient;

/**
 * Created by Ahmed on 5/25/2017.
 */

public class ApiUtils {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static MovieTrailerService getMovieTrailerService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieTrailerService.class);
    }

    public static MovieReviewService getMovieReviewsService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieReviewService.class);
    }
}
