package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private String[] moviesList;

    public MovieAdapter(String [] mMoviesList) {
        moviesList = mMoviesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);

            imageView = itemView;
        }
    }
        @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView image = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new MovieAdapter.ViewHolder(image);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        //holder.imageView.setImageResource(moviesList[position]);

    }

    @Override
    public int getItemCount() {
        return moviesList.length;
    }
}
