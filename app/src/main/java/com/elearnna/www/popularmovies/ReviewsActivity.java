package com.elearnna.www.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.elearnna.www.popularmovies.adapters.ReviewsAdapter;
import com.elearnna.www.popularmovies.data.model.UserReviews;

public class ReviewsActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ReviewsAdapter reviewsAdapter;
    private UserReviews reviews;
    private TextView movieTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width * .8), (int)(height * .8));

        movieTitle = (TextView) findViewById(R.id.review_movie_title);
        rv = (RecyclerView) findViewById(R.id.rv_reviews);
        reviewsAdapter = new ReviewsAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(reviewsAdapter);
        Intent intent = getIntent();
        if (intent.hasExtra("reviews")) {
            reviews = intent.getExtras().getParcelable("reviews");
            reviewsAdapter.setReviewsData(reviews);
            if (intent.hasExtra("movieTitle")) {
                movieTitle.setText(intent.getStringExtra("movieTitle"));
            }
        }
    }
}
