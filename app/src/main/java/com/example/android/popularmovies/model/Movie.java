package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie implements Parcelable{
    private double voteAverage;
    private double popularity;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private String releaseDate;

    public Movie(JSONObject movieObject) throws JSONException {
        this.voteAverage = movieObject.getDouble("vote_average");
        this.popularity = movieObject.getDouble("popularity");
        this.title = movieObject.getString("title");
        this.posterPath = movieObject.getString("poster_path");
        this.backdropPath = movieObject.getString("backdrop_path");
        this.overview = movieObject.getString("overview");
        this.releaseDate = movieObject.getString("release_date");
    }

    protected Movie(Parcel in) {
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static ArrayList<Movie> toMovie(ArrayList<JSONObject> movies) throws JSONException {
        ArrayList<Movie> movieList = new ArrayList<>();

        for(int i = 0; i < movies.size(); i++) {
            movieList.add(new Movie(movies.get(i)));
        }

        return movieList;
    }

    public double getVoteAverage(){
        return voteAverage;
    }
    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity(){
        return popularity;
    }
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath(){
        return posterPath;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath(){
        return backdropPath;
    }
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview(){
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate(){
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }
}
