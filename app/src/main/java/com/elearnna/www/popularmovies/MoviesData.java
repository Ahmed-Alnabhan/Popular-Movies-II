package com.elearnna.www.popularmovies;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.elearnna.www.popularmovies.adapters.MoviesAdapter.TAG;

/**
 * Created by Ahmed on 4/22/2017.
 */

public class MoviesData {
    static JSONObject[] moviesArray;

    // Get movies data from JSON
    protected static JSONObject[] getMoviesDataFromJSON(String moviesJSONString) {
        final String MOVIES_RESULTS = "results";
        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJSONString);
            JSONArray moviesJSONArray = moviesJsonObject.getJSONArray(MOVIES_RESULTS);

            moviesArray = new JSONObject[moviesJSONArray.length()];
            if (moviesJSONArray != null) {
                for (int i = 0; i < moviesJSONArray.length(); i++) {
                    JSONObject movieData = moviesJSONArray.getJSONObject(i);
                        moviesArray[i] = movieData;
                }
                for (JSONObject s : moviesArray) {
                    Log.v(TAG, "Poster's path: " + s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesArray;
    }
}
