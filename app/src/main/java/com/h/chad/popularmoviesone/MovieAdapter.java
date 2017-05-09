package com.h.chad.popularmoviesone;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.PublicKey;

/**
 * Created by chad on 5/8/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    public MovieAdapter(){

    }
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder{

        public MovieAdapterViewHolder(View view){
            super(view);
        }
    }

    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);

    }

    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position){

    }
    public int getItemCount(){
        if(null == null){
            return 0;
        }
        return 0;
    }

}
