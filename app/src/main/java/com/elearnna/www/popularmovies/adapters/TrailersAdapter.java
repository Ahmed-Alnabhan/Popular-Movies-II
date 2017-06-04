package com.elearnna.www.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elearnna.www.popularmovies.R;
import com.elearnna.www.popularmovies.data.model.MovieTrailer;
import com.elearnna.www.popularmovies.data.model.MovieTrailersResult;

/**
 * Created by Ahmed on 5/27/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    public static final String TAG = TrailersAdapter.class.getSimpleName();
    private Context context;
    private MovieTrailersResult trailersList;
    private TrailerAdapterClickListener trailerClickListener;

    // Trailer click listener interface
    public interface TrailerAdapterClickListener {
        void onClick(MovieTrailer movieTrailer);
    }

    // Constructor takes clickListener as a parameter
    public TrailersAdapter(TrailerAdapterClickListener listener) {
        this.trailerClickListener = listener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.trailers, parent, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        try {
            MovieTrailer trailer = trailersList.getResults().get(position);
            holder.trailerName.setText(trailer.getName().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(trailersList != null) {
            return trailersList.getResults().size();
        } else {
            return 0;
        }
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView trailerName;
        public TrailersViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            trailerName = (TextView) itemView.findViewById(R.id.trailerName);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieTrailer movieTrailerDetail = trailersList.getResults().get(adapterPosition);
            trailerClickListener.onClick(movieTrailerDetail);
        }
    }

    public void setTrailersData(MovieTrailersResult trailersData) {
        trailersList = trailersData;
        notifyDataSetChanged();
    }
}
