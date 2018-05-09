package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private ArrayList<Movie> moviesList;
    private Context context;


    public MovieAdapter(ArrayList<Movie> mMoviesList) {
        this.moviesList = mMoviesList;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.movie_poster_iv);
        }
    }


    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ImageView image = (ImageView) LayoutInflater.from(context)
                .inflate(R.layout.movie_item, parent, false);

        return new MovieAdapter.ViewHolder(image);
    }



    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie = moviesList.get(position);

        String imagePath = "http://image.tmdb.org/t/p/original" + movie.getBackdropPath();

        Picasso.with(context).load(imagePath).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
