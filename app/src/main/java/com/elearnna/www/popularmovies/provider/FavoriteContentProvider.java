package com.elearnna.www.popularmovies.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.elearnna.www.popularmovies.database.MoviesContract;
import com.elearnna.www.popularmovies.database.MoviesDBHelper;

import java.util.HashMap;

/**
 * Created by Ahmed on 6/2/2017.
 */

public class FavoriteContentProvider extends ContentProvider {
    static final String FAVORITE_PROVIDER_NAME = "com.elearnna.www.popularmovies.provider.FavoriteContentProvider";
    static final String URL = "content://" + FAVORITE_PROVIDER_NAME + "/favorites";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    // Database fields
    public static final String poster_path = "poster_path";
    public static final String overview = "overview";
    public static final String release_date = "release_date";
    public static final String id = "id";
    public static final String original_title = "original_title";
    public static final String original_language = "original_language";
    public static final String title = "title";
    public static final String backdrop_path = "backdrop_path";
    public static final String popularity = "popularity";
    public static final String vote_count = "vote_count";
    public static final String vote_average = "vote_average";
    static final int uriCode = 1;

    private static HashMap<String, String> values;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FAVORITE_PROVIDER_NAME, "favorites", uriCode);
    }
    private SQLiteDatabase mDb;
    private MoviesDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MoviesDBHelper(getContext());
        mDb = dbHelper.getWritableDatabase();
        return mDb != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesContract.MoviesEntry.TABLE_NAME);

        switch(uriMatcher.match(uri)){
            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/favorites";
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long recordId = mDb.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
        if (recordId > 0) {
            Uri myUri = ContentUris.withAppendedId(CONTENT_URI, recordId);
            getContext().getContentResolver().notifyChange(myUri, null);
            return myUri;
        } else {
            Toast.makeText(getContext(), "Faild to insert data", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                rowsDeleted = mDb.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                rowsUpdated = mDb.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
