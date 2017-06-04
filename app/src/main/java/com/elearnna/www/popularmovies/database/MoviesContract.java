package com.elearnna.www.popularmovies.database;

import android.provider.BaseColumns;

/**
 * Created by Ahmed on 5/31/2017.
 */

public class MoviesContract {
    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }
}
