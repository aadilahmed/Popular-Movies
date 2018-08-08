package com.example.android.popularmovies.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {
    private static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_PATH = "api_key";

    public static ArrayList<JSONObject> parseMovieJson(String json) throws JSONException {
        JSONObject movieList = new JSONObject(json);

        JSONArray movieDetailArray = movieList.getJSONArray("results");
        ArrayList<JSONObject> movies = new ArrayList<>();

        for(int i = 0; i < movieDetailArray.length(); i++) {
            movies.add(movieDetailArray.getJSONObject(i));
        }

        return movies;
    }


    public static ArrayList<JSONObject> parseTrailerJson(String json) throws JSONException {
        JSONObject trailerList = new JSONObject(json);

        JSONArray trailerDetailArray = trailerList.getJSONArray("results");
        ArrayList<JSONObject> trailers = new ArrayList<>();

        for(int i = 0; i < trailerDetailArray.length(); i++) {
            trailers.add(trailerDetailArray.getJSONObject(i));
        }

        return trailers;
    }

    public static URL buildURL(String SORT_PARAM, String API_KEY){

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(SORT_PARAM)
                .appendQueryParameter(API_PATH, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildTrailerURL(String MOVIE_ID, String VIDEO_PARAM, String API_KEY) {

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(MOVIE_ID)
                .appendPath(VIDEO_PARAM)
                .appendQueryParameter(API_PATH, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /*Citation: Android Developer Nanodegree Course Lesson 2.9*/
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput) {
                return scanner.next();
            }
            else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
