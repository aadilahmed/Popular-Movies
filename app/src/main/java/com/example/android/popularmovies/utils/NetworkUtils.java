package com.example.android.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    final static String POPULAR_PARAM = "popular";
    final static String RATING_PARAM = "rating";

    final static String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/";
    final static String POSTER_SIZE = "w185";

    // PUT API KEY HERE
    final static String API_KEY = "";
    final static String API_PATH = "?api_key=";

    public URL buildURL(){
        String dbSearchQuery = "";
        String apiPath = "";
        String apiKey = "";

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendQueryParameter(POPULAR_PARAM, dbSearchQuery)
                .appendQueryParameter(API_PATH, apiPath)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

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
