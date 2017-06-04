package com.elearnna.www.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 5/25/2017.
 */

public class MovieTrailersResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieTrailer> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }
}
