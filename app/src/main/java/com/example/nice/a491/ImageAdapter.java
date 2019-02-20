package com.example.nice.a491;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<upload> mUploads;

    public ImageAdapter(Context context, List<upload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, viewGroup, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        upload uploadCurrent = mUploads.get(i);
        Log.d("zzzzzzzzzz", "picasso  "+ uploadCurrent.getImageUrl());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.baseline_image_black_18dp)
                .fit().centerCrop()
                .into(imageViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends  RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageShow);
        }
    }

}


