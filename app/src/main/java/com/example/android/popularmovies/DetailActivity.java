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

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.Adapter mAdapter;

    private int movieId;

    private RecyclerView.ItemDecoration mDividerItemDecoration;


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

        mRecyclerView = findViewById(R.id.rv_trailer_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        Picasso.with(this).load(imagePath).into(backdropImage);

        titleTv.setText(movie.getTitle());
        plotSynopsisTv.setText(movie.getOverview());
        releaseDateTv.setText(movie.getReleaseDate());
        userRatingTv.setText(Double.toString(movie.getVoteAverage()));

        Picasso.with(this).load(posterPath).into(posterImage);

        new TrailerQueryTask().execute();
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
                }

                mAdapter = new TrailerAdapter(youtubeKeys);
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}