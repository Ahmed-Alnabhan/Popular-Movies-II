package com.elearnna.www.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesActivityFragment extends Fragment implements JsonResult, MoviesAdapter.MoviesAdapterOnClickHandler{
    public static final String TAG = MoviesActivityFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private Intent intent;
    private TextView tv_error_message;
    public MoviesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        tv_error_message = (TextView) view.findViewById(R.id.error_message);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);
        updateMoviesSort();
        return view;
    }

    @Override
    public void onFinishJsonReading(JSONObject[] s) {
        try {
            if (s != null) {
                moviesAdapter.setMoviesData(s);
                recyclerView.setVisibility(View.VISIBLE);
                tv_error_message.setVisibility(View.INVISIBLE);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                tv_error_message.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(JSONObject movieData) {
        Context context = getContext();
        intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("movie", movieData.toString());
        startActivity(intent);
    }
    private void updateMoviesSort(){
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
        String sort = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
            fetchMoviesTask.execute(sort);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesSort();
    }

}
