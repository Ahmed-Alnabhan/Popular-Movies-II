package com.elearnna.www.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.elearnna.www.popularmovies.adapters.MoviesAdapter;
import com.elearnna.www.popularmovies.provider.FavoriteContentProvider;

import org.json.JSONException;
import org.json.JSONObject;

public class MoviesActivity extends AppCompatActivity implements JsonResult,MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String SORT_FAVORITES = "favorites";
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private Intent intent;
    private TextView tv_error_message;
    private TextView tv_no_favorites_message;
    private boolean mTwoPane;
    private Bundle bundle, savedInstance;
    private MovieDetailActivityFragment detailFragment;
    private String selectedMovie;
    private FragmentManager fragmentManager;
    private Parcelable rvSavedstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bundle = new Bundle();
        savedInstance = savedInstanceState;
        // Get the screen orientation
        Activity moviesActivity = MoviesActivity.this;
        int screenOrientation = moviesActivity.getResources().getConfiguration().orientation;
        tv_error_message = (TextView) findViewById(R.id.error_message);
        tv_no_favorites_message = (TextView) findViewById(R.id.no_favorites_message);
        recyclerView = (RecyclerView)findViewById(R.id.rv_movies);
        if(screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        } else if(screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        }
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);
        if(savedInstanceState != null)
        {
            rvSavedstate = savedInstanceState.getParcelable("rvState");
            recyclerView.getLayoutManager().onRestoreInstanceState(rvSavedstate);
        }
        updateMoviesSort();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }@Override
    public void onFinishJsonReading(JSONObject[] s) {
        try {
            if (s != null) {
                selectedMovie = s[0].toString();
                if (findViewById(R.id.movie_detail_container) != null) {
                    bundle.putString("movie", selectedMovie);
                    mTwoPane = true;
            if (savedInstance == null) {
                fragmentManager = getSupportFragmentManager();
                detailFragment = new MovieDetailActivityFragment();
                detailFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .add(R.id.movie_detail_container, detailFragment).commit();
            }
                } else {
                    mTwoPane = false;
                }
                moviesAdapter.setMoviesData(s);
                recyclerView.setVisibility(View.VISIBLE);
                tv_error_message.setVisibility(View.INVISIBLE);
                tv_no_favorites_message.setVisibility(View.INVISIBLE);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                tv_error_message.setVisibility(View.VISIBLE);
                tv_no_favorites_message.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(JSONObject movieData) {
        bundle.putString("movie", movieData.toString());
        if (mTwoPane) {
            fragmentManager = getSupportFragmentManager();
            MovieDetailActivityFragment detailFragment = new MovieDetailActivityFragment();
            detailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment).commit();
        } else {
            Context context = getApplicationContext();
            intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private void updateMoviesSort(){
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
        String sort = PreferenceManager.getDefaultSharedPreferences(MoviesActivity.this)
                .getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        if (sort.equals(SORT_FAVORITES)) {
            JSONObject[] moviesData = readFavoritesFromProvider();
            onFinishJsonReading(moviesData);
        } else {
            fetchMoviesTask.execute(sort);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesSort();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("rvState", recyclerView.getLayoutManager().onSaveInstanceState());
    }

    private JSONObject[] readFavoritesFromProvider() {
        JSONObject[] favoriteMovies;
        JSONObject movieRow;
        int index = 0;
        Cursor cursor = getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null);
        favoriteMovies = new JSONObject[cursor.getCount()];
        if (cursor.moveToFirst()) {
            int numOfColumns = cursor.getColumnCount();
            do {
                movieRow = new JSONObject();
                for (int i = 1; i < numOfColumns; i++) {
                    try {
                        movieRow.put(cursor.getColumnName(i), cursor.getString(i));
                    } catch (JSONException jse) {
                        jse.printStackTrace();
                    }
                }
                favoriteMovies[index] = movieRow;
                index++;
            } while (cursor.moveToNext());
            tv_no_favorites_message.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            tv_no_favorites_message.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        return favoriteMovies;
    }

}
