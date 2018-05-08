package com.example.android.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static ArrayList<JSONObject> parseMovieJson(String json) throws JSONException {
        JSONObject movieList = new JSONObject(json);

        JSONArray movieDetailArray = movieList.getJSONArray("results");
        ArrayList<JSONObject> movies = new ArrayList<>();

        for(int i = 0; i < movieDetailArray.length(); i++) {
            movies.add(movieDetailArray.getJSONObject(i));
        }

        return movies;
    }
}
