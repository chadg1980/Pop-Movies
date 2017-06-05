package com.h.chad.PopMovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.h.chad.PopMovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.action;
import static android.R.attr.start;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.h.chad.PopMovies.utils.NetworkUtils.buildWatchUrl;
import static com.h.chad.PopMovies.utils.NetworkUtils.youtubeThumbnailEnd;
import static com.h.chad.PopMovies.utils.NetworkUtils.youtubeThumbnailUrlBase;
import static com.h.chad.PopMovies.utils.NetworkUtils.youtubeWatchUrlBase;

/**
 * Created by chad on 6/5/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private static String LOG_TAG = TrailerAdapter.class.getName();
    private ArrayList<String> mYoutubeId;
    private Context mContext;

    public TrailerAdapter(ArrayList<String> youtubeId, Context context){
        this.mYoutubeId = youtubeId;
        this.mContext = context;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForTrailerItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentImmediatly = false;
        View view = inflater.inflate(layoutIdForTrailerItem, parent, attachToParentImmediatly);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapterViewHolder holder, int position) {
        holder.bind(holder, position);


    }

    @Override
    public int getItemCount() {
        return mYoutubeId.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageButton mMovieThumbnail;
        TextView mTrailerTitle;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            this.mMovieThumbnail = (ImageButton) itemView.findViewById(R.id.ib_trailer_thumbnail);
            this.mTrailerTitle = (TextView) itemView.findViewById(R.id.tv_trailer_title);

        }
        void bind(TrailerAdapterViewHolder holder, int listIndex){
            Context context = itemView.getContext();
            String youtubeID = mYoutubeId.get(listIndex);
            final String thumbnailUrl = NetworkUtils.buildThumbUrl(youtubeID);
            final String watchYoutubeURl = buildWatchUrl(youtubeID);
            Picasso.with(context)
                    .load(thumbnailUrl)
                    .noFade()
                    .resize(240, 180)
                    .centerCrop()
                    .placeholder(R.drawable.ic_play_circle_outline_black_24dp)
                    .into(mMovieThumbnail);
            int trailerNumber = listIndex+1;
            mTrailerTitle.setText(mContext.getString(R.string.trailer_name) + trailerNumber);

            holder.mMovieThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playVideoIntent =
                            new Intent(Intent.ACTION_VIEW, Uri.parse(watchYoutubeURl));
                    mContext.startActivity(playVideoIntent);
                }
            });



        }



    }
}
