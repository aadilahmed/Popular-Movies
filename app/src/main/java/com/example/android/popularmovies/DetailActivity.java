package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {
    private static final String backdropBase = "http://image.tmdb.org/t/p/w1280";
    private static final String posterBase = "http://image.tmdb.org/t/p/w185";
    private static final String trailerBase = "https://www.youtube.com/watch?v=";

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

        String imagePath = backdropBase + movie.getBackdropPath();
        String posterPath = posterBase + movie.getPosterPath();

        Picasso.with(this).load(imagePath).into(backdropImage);

        titleTv.setText(movie.getTitle());
        plotSynopsisTv.setText(movie.getOverview());
        releaseDateTv.setText(movie.getReleaseDate());
        userRatingTv.setText(Double.toString(movie.getVoteAverage()));

        Picasso.with(this).load(posterPath).into(posterImage);
    }
}
