package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private ArrayList<Movie> moviesList;
    private Context context;
    private final static String imageBase = "http://image.tmdb.org/t/p/w185";

    public MovieAdapter(ArrayList<Movie> mMoviesList) {
        this.moviesList = mMoviesList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
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
        final Movie movie = moviesList.get(position);

        String imagePath = imageBase + movie.getPosterPath();

        Picasso.with(context).load(imagePath).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(moviesList == null) {
            return 0;
        }
        return moviesList.size();
    }
}
