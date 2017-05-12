package com.h.chad.popularmoviesone;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h.chad.popularmoviesone.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;

import static android.R.attr.start;
import static android.icu.lang.UProperty.AGE;
import static android.media.CamcorderProfile.get;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by chad on 5/8/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getName();
    public final static String MOVIE_TITLE = "MOVIE_TITLE";
    public final static String MOVIE_RELEASE_DATE = "MOVIE_RELEASE_DATE";
    public final static String MOVIE_POSTER_PATH = "MOVIE_POSTER_PATH";
    public final static String MOVIE_VOTE_AVERAGE = "MOVIE_VOTE_AVERAGE";
    public final static String MOVIE_PLOT = "MOVIE_PLOT";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w342";
    private ArrayList<Movie> mMovie;
    private Context mContext;

    public MovieAdapter(ArrayList<Movie> movie, Context context){
        this.mMovie = movie;
        this.mContext = context;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForRecyclerViewItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentImmediatly = false;
        View view = inflater.inflate(layoutIdForRecyclerViewItem, parent, attachToParentImmediatly);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(mContext, MovieDetail.class);
                String movieTitle = mMovie.get(position).getTitle();
                String movieReleaseDate = mMovie.get(position).getReleaseDate();
                String posterPath = mMovie.get(position).getPosterPath();
                double movieVoteAverage = mMovie.get(position).getVoteAverage();
                String moviePlot = mMovie.get(position).getPlot();

                intent.putExtra(MOVIE_TITLE, movieTitle);
                intent.putExtra(MOVIE_RELEASE_DATE, movieReleaseDate);
                intent.putExtra(MOVIE_POSTER_PATH, posterPath);
                intent.putExtra(MOVIE_VOTE_AVERAGE, movieVoteAverage);
                intent.putExtra(MOVIE_PLOT, moviePlot);
                mContext.startActivity(intent);
            }
        });
        holder.bind(position);
    }

    @Override
    public int getItemCount(){
        return mMovie.size();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
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
