package com.h.chad.popularmoviesone;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h.chad.popularmoviesone.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by chad on 5/8/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getName();
    private ArrayList<Movie> mMovie;
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    public MovieAdapter(ArrayList<Movie> movie){
        this.mMovie = movie;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        int layoutIdForRecyclerViewItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(c);
        boolean attachToParentImmediatly = false;


        View view = inflater.inflate(layoutIdForRecyclerViewItem, parent, attachToParentImmediatly);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovie.size();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView mPoster;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView)itemView.findViewById(R.id.rv_poster);
        }
        void bind(int listIndex){
            Context context = itemView.getContext();
            String poster = mMovie.get(listIndex).getPosterPath();
            String totalUrl = IMAGE_URL + poster;
            Picasso.with(context).load(totalUrl).into(mPoster);
        }
    }
}
