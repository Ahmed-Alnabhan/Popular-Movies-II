package com.elearnna.www.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elearnna.www.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by Ahmed on 4/20/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{
    public static final String TAG = MoviesAdapter.class.getSimpleName();
    private final String PIC_URL = "https://image.tmdb.org/t/p/w185";
    private final String POSTER_PATH = "poster_path";
    private JSONObject[] moviesInfoData;
    private Context context;
    private MoviesAdapterOnClickHandler movieClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(JSONObject movieData);
    }

    // Constructor takes clickHandler as a parameter
    public MoviesAdapter(MoviesAdapterOnClickHandler onClickHandler) {
        movieClickHandler = onClickHandler;
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movies_posters, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position){
        try {
            JSONObject moviePoster = moviesInfoData[position];
            String moviePosterUrl = PIC_URL + moviePoster.getString(POSTER_PATH);
            Picasso.with(context).load(moviePosterUrl).placeholder(R.drawable.loading_185).error(R.drawable.unavailable_185).fit().into(holder.movieImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(moviesInfoData != null) {
            return moviesInfoData.length;
        } else {
            return 0;
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView movieImageView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movieImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            JSONObject movieDetail = moviesInfoData[adapterPosition];
            movieClickHandler.onClick(movieDetail);
        }
    }

    /**
     * This method is used to set the movies posters on a MoviesAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MoviesAdapter to display it.
     *
     * @param moviesData The new movies data to be displayed.
     */
    public void setMoviesData(JSONObject[] moviesData) {
        moviesInfoData = moviesData;
        notifyDataSetChanged();
    }


}
