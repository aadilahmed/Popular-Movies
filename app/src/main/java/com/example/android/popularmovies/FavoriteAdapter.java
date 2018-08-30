package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.database.FavoriteEntry;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<FavoriteEntry> favoriteEntryList;
    private Context context;
    private final static String imageBase = "http://image.tmdb.org/t/p/w185";

    public FavoriteAdapter(List<FavoriteEntry> mFavoriteEntryList) {
        this.favoriteEntryList = mFavoriteEntryList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_poster_iv);
        }
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ImageView image = (ImageView) LayoutInflater.from(context)
                .inflate(R.layout.movie_item, parent, false);

        return new FavoriteAdapter.ViewHolder(image);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.ViewHolder holder, int position) {
        final FavoriteEntry current = favoriteEntryList.get(position);
        double voteAverage = current.getVoteAverage();
        int id = current.getId();
        String title = current.getTitle();
        String posterPath = current.getPosterPath();
        String backdropPath = current.getBackdropPath();
        String overview = current.getOverview();
        String releaseDate = current.getReleaseDate();

        final Movie movie;

        movie = new Movie(voteAverage, id, title, posterPath, backdropPath, overview, releaseDate);

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
        if(favoriteEntryList == null) {
            return 0;
        }
        return favoriteEntryList.size();
    }

    public List<FavoriteEntry> getFavorites() {
        return favoriteEntryList;
    }

}
