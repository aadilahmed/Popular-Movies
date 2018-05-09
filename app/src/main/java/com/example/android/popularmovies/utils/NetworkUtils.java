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
    private static final String POPULAR_PARAM = "popular";
    private static final String RATING_PARAM = "rating";

    private static final String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    // PUT API KEY HERE
    private static final String API_KEY = "";
    private static final String API_PATH = "?api_key=";
    private static final String equals = "=";

    public static ArrayList<JSONObject> parseMovieJson(String json) throws JSONException {
        JSONObject movieList = new JSONObject(json);

        JSONArray movieDetailArray = movieList.getJSONArray("results");
        ArrayList<JSONObject> movies = new ArrayList<>();

        for(int i = 0; i < movieDetailArray.length(); i++) {
            movies.add(movieDetailArray.getJSONObject(i));
        }

        return movies;
    }

    public static URL buildURL(){

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL + POPULAR_PARAM + API_PATH + API_KEY);/*Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(POPULAR_PARAM)
                .appendEncodedPath(API_PATH)
                .appendPath(API_KEY)
                .build();*/

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

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
