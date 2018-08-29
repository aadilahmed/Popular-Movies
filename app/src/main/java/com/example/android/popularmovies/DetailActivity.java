package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavoriteEntry;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.popularmovies.utils.NetworkUtils.parseTrailerJson;


public class DetailActivity extends AppCompatActivity {
    private static final String API_KEY = BuildConfig.APIKEY;
    private static final String backdropBase = "http://image.tmdb.org/t/p/w1280";
    private static final String posterBase = "http://image.tmdb.org/t/p/w185";

    private static final String VIDEO_PARAM = "videos";
    private static final String REVIEW_PARAM = "reviews";

    private ArrayList<JSONObject> trailers = new ArrayList<>();
    private ArrayList<String> youtubeKeys = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<JSONObject> reviews = new ArrayList<>();
    private ArrayList<String> reviewContent = new ArrayList<>();
    private ArrayList<String> reviewURL = new ArrayList<>();
    private ArrayList<String> authors = new ArrayList<>();

    private RecyclerView mTrailerRV;
    private RecyclerView mReviewRV;
    private RecyclerView.LayoutManager mTrailerLayoutManager;
    private RecyclerView.LayoutManager mReviewLayoutManager;

    private RecyclerView.Adapter mTrailerAdapter;
    private RecyclerView.Adapter mReviewAdapter;

    private int movieId;

    private RecyclerView.ItemDecoration mTrailerDividerItemDecoration;
    private RecyclerView.ItemDecoration mReviewDividerItemDecoration;

    private AppDatabase mDb;

    private Boolean movieIsFavorited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = AppDatabase.getInstance(getApplicationContext());

        ImageView backdropImage = findViewById(R.id.backdrop_iv);
        ImageView posterImage = findViewById(R.id.detail_poster_iv);

        TextView titleTv = findViewById(R.id.title_tv);
        TextView plotSynopsisTv = findViewById(R.id.plot_synopsis_tv);
        TextView releaseDateTv = findViewById(R.id.release_date_tv);
        TextView userRatingTv = findViewById(R.id.user_rating_tv);

        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("movie");

        movieId = movie.getId();

        String imagePath = backdropBase + movie.getBackdropPath();
        String posterPath = posterBase + movie.getPosterPath();

        mTrailerRV = findViewById(R.id.rv_trailer_list);
        mTrailerRV.setHasFixedSize(true);

        mReviewRV = findViewById(R.id.rv_review_list);
        mReviewRV.setHasFixedSize(true);

        mTrailerLayoutManager = new LinearLayoutManager(this);
        mTrailerRV.setLayoutManager(mTrailerLayoutManager);

        mReviewLayoutManager = new LinearLayoutManager(this);
        mReviewRV.setLayoutManager(mReviewLayoutManager);

        mTrailerDividerItemDecoration = new DividerItemDecoration(mTrailerRV.getContext(),
                DividerItemDecoration.VERTICAL);
        mTrailerRV.addItemDecoration(mTrailerDividerItemDecoration);

        mReviewDividerItemDecoration = new DividerItemDecoration(mReviewRV.getContext(),
                DividerItemDecoration.VERTICAL);
        mReviewRV.addItemDecoration(mReviewDividerItemDecoration);

        Picasso.with(this).load(imagePath).into(backdropImage);

        titleTv.setText(movie.getTitle());
        plotSynopsisTv.setText(movie.getOverview());
        releaseDateTv.setText(movie.getReleaseDate());
        userRatingTv.setText(Double.toString(movie.getVoteAverage()));

        Picasso.with(this).load(posterPath).into(posterImage);

        new TrailerQueryTask().execute();
        new ReviewQueryTask().execute();

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.pref_file_key),
                Context.MODE_PRIVATE);
        Boolean defaultVal = getResources().getBoolean(R.bool.isFavorited);
        movieIsFavorited = sharedPref.getBoolean(getString(R.string.favorite_key), defaultVal);

        Context context = this;
        Button button = findViewById(R.id.favorite_button);
        onFavoritesButtonClicked(movie, button);
    }


    public void onFavoritesButtonClicked(Movie movie, final Button button) {
        final double voteAverage = movie.getVoteAverage();
        final int tmdbId = movie.getId();
        final String title = movie.getTitle();
        final String posterPath = movie.getPosterPath();
        final String backdropPath = movie.getBackdropPath();
        final String overview = movie.getOverview();
        final String releaseDate = movie.getReleaseDate();

        if(movieIsFavorited) {
            button.setText(R.string.already_favorited);
        }
        else{
            button.setText(R.string.favorite_text);
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setText(R.string.already_favorited);

                final FavoriteEntry favoriteEntry = new FavoriteEntry(tmdbId, voteAverage, title,
                        posterPath, backdropPath, overview, releaseDate);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.favoriteDao().insertFavorite(favoriteEntry);
                    }
                });
            }
        });
    }

    public class TrailerQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = NetworkUtils.buildTrailerURL(Integer.toString(movieId), VIDEO_PARAM, API_KEY);

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
                trailers = parseTrailerJson(s);

                for(int i = 0; i < trailers.size(); i++){
                    youtubeKeys.add(trailers.get(i).getString("key"));
                    titles.add(trailers.get(i).getString("name"));
                }

                if(youtubeKeys.isEmpty()) {
                    youtubeKeys.add(getResources().getString(R.string.no_trailer_text));
                    titles.add("");
                }

                mTrailerAdapter = new TrailerAdapter(youtubeKeys, titles);
                mTrailerRV.setAdapter(mTrailerAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReviewQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = NetworkUtils.buildTrailerURL(Integer.toString(movieId), REVIEW_PARAM, API_KEY);

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
                reviews = parseTrailerJson(s);

                for(int i = 0; i < reviews.size(); i++){
                    reviewContent.add(reviews.get(i).getString("content"));
                    reviewURL.add(reviews.get(i).getString("url"));
                    authors.add(reviews.get(i).getString("author"));
                }

                if(reviewContent.isEmpty()) {
                    reviewContent.add("");
                    reviewURL.add("");
                    authors.add(getResources().getString(R.string.no_review_text));
                }

                mReviewAdapter = new ReviewAdapter(reviewContent, reviewURL, authors);
                mReviewRV.setAdapter(mReviewAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}