package com.elearnna.www.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed on 5/31/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    // Define the database name
    private static final String DATABASE_NAME = "popular_movies.db";
    // Define the database version
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create string query that creates the movies table
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_POPULARITY + " REAL, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL" +
                ");";
        // Execute the query to create the table
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
