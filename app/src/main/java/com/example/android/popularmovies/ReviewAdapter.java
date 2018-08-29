package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<String> reviewList;
    private ArrayList<String> urlList;
    private ArrayList<String> authorList;
    private Context context;

    public ReviewAdapter(ArrayList<String> mReviews, ArrayList<String> mURL, ArrayList<String> mAuthors) {
        this.reviewList = mReviews;
        this.urlList = mURL;
        this.authorList = mAuthors;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.review_content_tv);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.review_item, parent, false);

        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        final String author = authorList.get(position);
        final String review = reviewList.get(position);
        String url = urlList.get(position);

        final Uri reviewLink = Uri.parse(url);

        holder.textView.setText(author);
        holder.textView.append("\n\n" + review);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, reviewLink);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(reviewList == null) {
            return 0;
        }
        return reviewList.size();
    }
}
