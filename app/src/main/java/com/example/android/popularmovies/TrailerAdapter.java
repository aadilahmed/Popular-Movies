package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{
    private ArrayList<String> trailerList;
    private ArrayList<String> titleList;
    private Context context;
    private final static String trailerBase = "https://www.youtube.com/watch?v=";

    public TrailerAdapter(ArrayList<String> mKeyList, ArrayList<String> mTitleList) {
        this.trailerList = mKeyList;
        this.titleList = mTitleList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trailer_link_iv);
            textView = itemView.findViewById(R.id.trailer_number_tv);
        }
    }


    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.trailer_item, parent, false);

        return new TrailerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder holder, int position) {
        final String key = trailerList.get(position);
        String trailerPath = trailerBase + key;

        final String trailerTitle = titleList.get(position);

        final Uri trailerLink = Uri.parse(trailerPath);

        /*https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/TriangleArrow-White-Right.svg
          /2000px-TriangleArrow-White-Right.svg.png*/
        Picasso.with(context)
                .load(R.drawable.white_triangle)
                .into(holder.imageView);

        holder.textView.setText(trailerTitle);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, trailerLink);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(trailerList == null) {
            return 0;
        }
        return trailerList.size();
    }
}
