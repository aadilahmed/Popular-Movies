package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.popularmovies.utils.NetworkUtils.parseTrailerJson;


public class DetailActivity extends AppCompatActivity {
    private static final String API_KEY = "";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
    }


    public class TrailerQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = NetworkUtils.buildTrailerURL(Integer.toString(movieId), VIDEO_PARAM, API_KEY);

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
                trailers = parseTrailerJson(s);

                for(int i = 0; i < trailers.size(); i++){
                    youtubeKeys.add(trailers.get(i).getString("key"));
                    titles.add(trailers.get(i).getString("name"));
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
                reviews = parseTrailerJson(s);

                for(int i = 0; i < reviews.size(); i++){
                    reviewContent.add(reviews.get(i).getString("content"));
                    reviewURL.add(reviews.get(i).getString("url"));
                    authors.add(reviews.get(i).getString("author"));
                }

                mReviewAdapter = new ReviewAdapter(reviewContent, reviewURL, authors);
                mReviewRV.setAdapter(mReviewAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}