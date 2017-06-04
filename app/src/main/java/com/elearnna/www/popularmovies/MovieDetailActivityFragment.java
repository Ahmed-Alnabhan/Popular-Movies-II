package com.elearnna.www.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elearnna.www.popularmovies.adapters.TrailersAdapter;
import com.elearnna.www.popularmovies.data.model.MovieTrailer;
import com.elearnna.www.popularmovies.data.model.MovieTrailersResult;
import com.elearnna.www.popularmovies.data.model.UserReviews;
import com.elearnna.www.popularmovies.data.network.MovieReviewService;
import com.elearnna.www.popularmovies.data.network.MovieTrailerService;
import com.elearnna.www.popularmovies.database.MoviesDBHelper;
import com.elearnna.www.popularmovies.provider.FavoriteContentProvider;
import com.elearnna.www.popularmovies.utilities.ApiUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends android.support.v4.app.Fragment implements TrailersAdapter.TrailerAdapterClickListener{
    public static final String TAG_MDAF = MovieDetailActivityFragment.class.getSimpleName();
    private static final String TRAILER_TYPE = "Trailer";
    private static final String YOUTUBE_BASE_BROWSER_URL = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_BASE_APP_URL = "vnd.youtube:";
    // data of selected movie
    private boolean mFavorite;
    private String posterPath;
    private String title;
    private String releaseDate;
    private String originalTitle;
    private String originalLanguage;
    private String backdropPath;
    private double popularity;
    private int voteCount;
    private int movieId;
    private String voteAverage;
    private String overview;
    private final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private String posterFullPath;
    // Widgets and fields
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieDate;
    private TextView movieVoteRate;
    private TextView moviePreview;
    private ImageView mLikeImage;
    private ImageView mUnLikeImage;
    private Intent intent;
    private String myMovie;
    private MovieTrailersResult movieTrailers;
    private UserReviews movieReviews;
    private MovieTrailersResult finalMovieTrailers;
    private MovieTrailerService movieTrailerService;
    private MovieReviewService movieReviewService;
    private RecyclerView rv;
    private TrailersAdapter adapter;
    private List<MovieTrailer> trailersType;
    private View userReviews;
    private String apiKey;
    private Intent reviewIntent;
    // Define DB
    private SQLiteDatabase mDb;
    private JSONObject jsonMovie;
    private MoviesDBHelper dbHelper;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mLikeImage = (ImageView) view.findViewById(R.id.favorite_full);
        mUnLikeImage = (ImageView) view.findViewById(R.id.favorite_stroke);
        apiKey = BuildConfig.API_KEY;
        // define database stuff
        dbHelper = new MoviesDBHelper(getContext());
        mDb = dbHelper.getWritableDatabase();
        userReviews =  view.findViewById(R.id.userReviewsLayout);
        intent = getActivity().getIntent();
        if(intent.hasExtra("movie")){
            myMovie = intent.getStringExtra("movie");
        } else {
            myMovie = getArguments().getString("movie");
        }
            Context context = getActivity();
        if (!(myMovie.equals(null))) {
            try {
                jsonMovie = new JSONObject(myMovie);

                movieId = jsonMovie.getInt("id");
                isMovieFavorite(String.valueOf(movieId));
                posterPath = jsonMovie.getString("poster_path");
                title = jsonMovie.getString("title");
                releaseDate = jsonMovie.getString("release_date");
                voteAverage = jsonMovie.getString("vote_average");
                overview = jsonMovie.getString("overview");
                originalTitle = jsonMovie.getString("original_title");
                originalLanguage = jsonMovie.getString("original_language");
                backdropPath = jsonMovie.getString("backdrop_path");
                popularity = jsonMovie.getDouble("popularity");
                voteCount = jsonMovie.getInt("vote_count");

                posterFullPath = MOVIE_POSTER_BASE_URL.concat(posterPath);
                moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
                movieTitle = (TextView) view.findViewById(R.id.movieTitle);
                movieDate = (TextView) view.findViewById(R.id.movieDate);
                movieVoteRate = (TextView) view.findViewById(R.id.movieVoteRate);
                moviePreview = (TextView) view.findViewById(R.id.movieThumb);

                movieTitle.setText(title);
                movieDate.setText(releaseDate);
                movieVoteRate.setText(voteAverage + "/10");
                // Make the preview text view scrollable
                moviePreview.setMovementMethod(new ScrollingMovementMethod());
                moviePreview.setText(overview);
                mLikeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFavorite) {
                            unlikeIt();
                        }
                    }
                });

                mUnLikeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mFavorite) {
                            likeIt();
                        }
                    }
                });

                Picasso.with(context)
                        .load(posterFullPath)
                        .placeholder(R.drawable.loading_185)
                        .error(R.drawable.unavailable_popular_movies200)
                        .into(moviePoster);
                rv = (RecyclerView) view.findViewById(R.id.rv_trailers);
                adapter = new TrailersAdapter(this);
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                rv.setLayoutManager(manager);
                rv.setHasFixedSize(true);
                rv.setAdapter(adapter);
                trailersType = new ArrayList<>();
                finalMovieTrailers = new MovieTrailersResult();
                loadMovieTrailers();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        userReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoviesReviews();
            }
        });

        return view;
    }

    // Unlike it method
    private void unlikeIt() {
        deleteMovieFromDB(movieId);
        unfavoriteMovie();
    }

    // likeIt method
    private void likeIt() {
        addMovieToDB();
        favoriteMovie();
    }

    // Load movie trailers
    public void loadMovieTrailers() {
        movieTrailerService = ApiUtils.getMovieTrailerService();
        Call <MovieTrailersResult> call = movieTrailerService.getMovieTrailers(movieId, apiKey);
        call.enqueue(new Callback<MovieTrailersResult>() {

            @Override
            public void onResponse(Call<MovieTrailersResult> call, Response<MovieTrailersResult> response) {
                if (response.isSuccessful()){
                    movieTrailers = response.body();
                    MovieTrailer currentTrailer;
                    for (int i = 0; i < movieTrailers.getResults().size(); i++) {
                        currentTrailer = movieTrailers.getResults().get(i);
                        if (currentTrailer.getType().equals(TRAILER_TYPE)) {
                            trailersType.add(currentTrailer);
                        }
                    }
                    finalMovieTrailers.setResults(trailersType);
                    adapter.setTrailersData(finalMovieTrailers);


                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<MovieTrailersResult> call, Throwable t) {

            }
        });
    }

    // Load movies reviews
    private void loadMoviesReviews() {
        movieReviewService = ApiUtils.getMovieReviewsService();
        Call<UserReviews> call = movieReviewService.getMovieReviews(movieId, apiKey);
        call.enqueue(new Callback<UserReviews>(){

            @Override
            public void onResponse(Call<UserReviews> call, Response<UserReviews> response) {
                if (response.isSuccessful()) {
                    movieReviews = response.body();
                    reviewIntent = new Intent(MovieDetailActivityFragment.this.getContext(), ReviewsActivity.class);
                    reviewIntent.putExtra("reviews", movieReviews);
                    reviewIntent.putExtra("movieTitle", title);
                    startActivity(reviewIntent);
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<UserReviews> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public void onClick(MovieTrailer movieTrailer) {
        String trailerYoutubeKey = getTrailerYoutubeKey(movieTrailer);
        playYoutubeTrailer(trailerYoutubeKey);
    }

    private String getTrailerYoutubeKey(MovieTrailer movieTrailer) {
        String movieTrailerKey = movieTrailer.getKey();
        return movieTrailerKey;
    }

    private void playYoutubeTrailer(String youtubeKey) {
        Intent intentApp = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_APP_URL + youtubeKey));
        Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_BROWSER_URL + youtubeKey));
        try {
            startActivity(intentApp);
        } catch (ActivityNotFoundException ex) {
            startActivity(intentBrowser);
        }
    }

    private void addMovieToDB() {
        // Create a new map of values
        ContentValues movieValues = new ContentValues();
        movieValues.put(FavoriteContentProvider.poster_path, posterPath);
        movieValues.put(FavoriteContentProvider.overview, overview);
        movieValues.put(FavoriteContentProvider.release_date, releaseDate);
        movieValues.put(FavoriteContentProvider.id, movieId);
        movieValues.put(FavoriteContentProvider.original_title, originalTitle);
        movieValues.put(FavoriteContentProvider.original_language, originalLanguage);
        movieValues.put(FavoriteContentProvider.title, title);
        movieValues.put(FavoriteContentProvider.backdrop_path, backdropPath);
        movieValues.put(FavoriteContentProvider.popularity, popularity);
        movieValues.put(FavoriteContentProvider.vote_count, voteCount);
        movieValues.put(FavoriteContentProvider.vote_average, voteAverage);

        //long newRowId = mDb.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValues);
        Uri uri = getContext().getContentResolver().insert(FavoriteContentProvider.CONTENT_URI, movieValues);
    }

    private void deleteMovieFromDB(int movId) {
        String selection = FavoriteContentProvider.id + " LIKE ?";
        String[] selectionArgs = {String.valueOf(movId)};
        getContext().getContentResolver().delete(FavoriteContentProvider.CONTENT_URI, selection, selectionArgs);
    }

    private void isMovieFavorite(String movId) {
        String[] projection = {"id"};
        String selection = FavoriteContentProvider.id + " LIKE ?";
        String[] selectionArgs = {movId};
        Cursor cursor = getContext().getContentResolver().query(FavoriteContentProvider.CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            favoriteMovie();
        } else {
            unfavoriteMovie();
        }
    }

    private void favoriteMovie() {
        mFavorite = true;
        mLikeImage.setVisibility(View.VISIBLE);
        mUnLikeImage.setVisibility(View.INVISIBLE);
    }

    private void unfavoriteMovie() {
        mFavorite = false;
        mLikeImage.setVisibility(View.INVISIBLE);
        mUnLikeImage.setVisibility(View.VISIBLE);
    }
}
