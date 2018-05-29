package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Put API key here
    private static final String API_KEY = "";
    private String SORT_PARAM = "popular";
    private static final String NO_CONNECTION_TOAST = "No network connectivity. Please connect to the internet"
            + " and restart the app.";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<JSONObject> movies = new ArrayList<>();
    private ArrayList<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_grid);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new MovieQueryTask().execute();
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = NetworkUtils.buildURL(SORT_PARAM, API_KEY);

            if(!checkInternet()) {
                cancel(true);
            }

            try {
                String response = NetworkUtils.getResponseFromHttpUrl(url);

                return response;
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                movies = NetworkUtils.parseMovieJson(s);

                movieList = Movie.toMovie(movies);

                mAdapter = new MovieAdapter(movieList);
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast toast = Toast.makeText(getApplicationContext(), NO_CONNECTION_TOAST, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.sort_by, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.popular) {
            SORT_PARAM = "popular";
        }
        if(item.getItemId() == R.id.top_rated) {
            SORT_PARAM = "top_rated";
        }

        new MovieQueryTask().execute();

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("sort", SORT_PARAM);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        SORT_PARAM = savedInstanceState.getString("sort");
        new MovieQueryTask().execute();
    }

    /* Citation: https://stackoverflow.com/questions
                 /1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out*/
    public boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}