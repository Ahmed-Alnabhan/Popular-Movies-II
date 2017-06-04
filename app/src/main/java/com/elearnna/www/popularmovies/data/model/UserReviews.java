package com.elearnna.www.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 5/27/2017.
 */

public class UserReviews implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<UserReview> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<UserReview> getResults() {
        return results;
    }

    public void setResults(List<UserReview> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    protected UserReviews(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        page = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<UserReview>();
            in.readList(results, UserReview.class.getClassLoader());
        } else {
            results = null;
        }
        totalPages = in.readByte() == 0x00 ? null : in.readInt();
        totalResults = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (page == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(page);
        }
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        if (totalPages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalPages);
        }
        if (totalResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalResults);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserReviews> CREATOR = new Parcelable.Creator<UserReviews>() {
        @Override
        public UserReviews createFromParcel(Parcel in) {
            return new UserReviews(in);
        }

        @Override
        public UserReviews[] newArray(int size) {
            return new UserReviews[size];
        }
    };
}