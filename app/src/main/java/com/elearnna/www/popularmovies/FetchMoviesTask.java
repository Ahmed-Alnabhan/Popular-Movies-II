package com.elearnna.www.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ahmed on 4/19/2017.
 */

public class FetchMoviesTask extends AsyncTask<String, Void, JSONObject[]> {
    private final String TAG = FetchMoviesTask.class.getSimpleName();

    String moviesJsonString;
    private JsonResult listener;
    // Constructor
    public FetchMoviesTask(JsonResult listener) {
        this.listener = listener;
    }
    @Override
    protected JSONObject[] doInBackground(String... params) {
        JSONObject[] moviesList = null;
        if(params.length == 0) {
            return null;
        }
        if (isOnline()) {
            moviesJsonString = connectAndReadJSON(params[0]);
            moviesList = MoviesData.getMoviesDataFromJSON(moviesJsonString);
        }
        return moviesList;
    }

    // Connect to the endpoint and read the Json
    private String connectAndReadJSON(String sortType) {
        HttpURLConnection urlConnection = null;
        String movie = null;
        try {
            // Create the url
            final String MOVIES_BASE_RUL = "http://api.themoviedb.org/3/movie/";
            final String MOVIES_SORT_METHOD = "sort_by";
            final String API_KEY = "api_key";

            Uri uri = Uri.parse(MOVIES_BASE_RUL).buildUpon()
                    .appendEncodedPath(sortType)
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build();
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                movie = scanner.next();
            } else {
                movie =  null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return movie;
    }
    @Override
    protected void onPostExecute(JSONObject[] strings) {
        super.onPostExecute(strings);
        listener.onFinishJsonReading(strings);
    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress socketaddress = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(socketaddress, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }
}
