package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String imageBase = "http://image.tmdb.org/t/p/original";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView backdropImage = (ImageView) findViewById(R.id.backdrop_iv);

        TextView titleTv = findViewById(R.id.title_tv);
        TextView plotSynopsisTv = (TextView) findViewById(R.id.plot_synopsis_tv);
        TextView releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        TextView userRatingTv = (TextView) findViewById(R.id.user_rating_tv);

        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("movie");

        String imagePath = imageBase + movie.getBackdropPath();

        Picasso.with(this).load(imagePath).into(backdropImage);

        titleTv.setText(movie.getTitle());
        plotSynopsisTv.setText(movie.getOverview());
    }
}
