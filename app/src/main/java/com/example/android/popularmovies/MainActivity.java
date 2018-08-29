package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavoriteEntry;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.APIKEY;
    private String SORT_PARAM = "popular";
    private static final String NO_CONNECTION_TOAST = "No network connectivity. Please connect to the internet"
            + " and restart the app.";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private FavoriteAdapter fAdapter;
    private ArrayList<JSONObject> movies = new ArrayList<>();
    private ArrayList<Movie> movieList = new ArrayList<>();

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_grid);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null) {
            SORT_PARAM = savedInstanceState.getString("sort");
        }

        if(SORT_PARAM.equals("favorites")) {
            setupViewModel();
            onFavoriteSwiped();
        }
        else {
            new MovieQueryTask().execute();
        }
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = NetworkUtils.buildURL(SORT_PARAM, API_KEY);

            if(!checkInternet()) {
                cancel(true);
            }

            try {
                return NetworkUtils.getResponseFromHttpUrl(url);
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
        switch(item.getItemId()){
            case R.id.popular:
                SORT_PARAM = "popular";
                new MovieQueryTask().execute();
                break;
            case R.id.top_rated:
                SORT_PARAM = "top_rated";
                new MovieQueryTask().execute();
                break;
            case R.id.favorites:
                SORT_PARAM = "favorites";
                setupViewModel();
                onFavoriteSwiped();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("sort", SORT_PARAM);
    }


    /* Citation: https://stackoverflow.com/questions
                     /1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out*/
    public boolean checkInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void onFavoriteSwiped() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<FavoriteEntry> favorites = fAdapter.getFavorites();
                        mDb.favoriteDao().deleteFavorite(favorites.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                fAdapter = new FavoriteAdapter(favoriteEntries);
                mRecyclerView.setAdapter(fAdapter);
            }
        });
    }
}