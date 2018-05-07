package com.example.android.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {
    private int voteAverage;
    private int popularity;
    private String title;
    private String backdropPath;
    private String overview;
    private String releaseDate;

    public Movie(JSONObject movieObject) throws JSONException {
        this.voteAverage = movieObject.getInt("vote_average");
        this.popularity = movieObject.getInt("popularity");
        this.title = movieObject.getString("title");
        this.backdropPath = movieObject.getString("backdrop_path");
        this.overview = movieObject.getString("overview");
        this.releaseDate = movieObject.getString("releaseDate");
    }

    public ArrayList<Movie> toMovie(ArrayList<JSONObject> movies) throws JSONException {
        ArrayList<Movie> movieList = new ArrayList<>();

        for(int i = 0; i < movies.size(); i++) {
            movieList.add(new Movie(movies.get(i)));
        }

        return movieList;
    }

    public int getVoteAverage(){
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getPopularity(){
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
}
