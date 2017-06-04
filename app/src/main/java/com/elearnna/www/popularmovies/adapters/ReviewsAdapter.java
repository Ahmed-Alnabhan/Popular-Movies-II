package com.elearnna.www.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elearnna.www.popularmovies.R;
import com.elearnna.www.popularmovies.data.model.UserReview;
import com.elearnna.www.popularmovies.data.model.UserReviews;

/**
 * Created by Ahmed on 5/29/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    public static final String TAG = ReviewsAdapter.class.getSimpleName();
    private Context context;
    private UserReviews reviews;

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.reviews, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        try {
            UserReview review = reviews.getResults().get(position);
            holder.reviewAuthor.setText(review.getAuthor().toString());
            holder.reviewContent.setText(review.getContent().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(reviews != null) {
            return reviews.getResults().size();
        } else {
            return 0;
        }
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        public final TextView reviewAuthor;
        public final TextView reviewContent;
        public ReviewsViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    public void setReviewsData(UserReviews reviewsData) {
        reviews = reviewsData;
        notifyDataSetChanged();
    }
}
