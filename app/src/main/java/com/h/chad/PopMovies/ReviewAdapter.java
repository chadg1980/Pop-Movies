package com.h.chad.PopMovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by chad on 6/5/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static String LOG_TAG = ReviewAdapter.class.getName();
    private ArrayList<Review> mReviewsArray;
    private Context mContext;
    private boolean mExpanded = false;


    public ReviewAdapter(ArrayList<Review> reviewArray, Context context ){
        this.mReviewsArray = reviewArray;
        this.mContext = context;
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdforReview = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentImmediatly = false;
        View view = inflater.inflate(layoutIdforReview, parent, attachToParentImmediatly);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewAdapterViewHolder holder, int position) {

        holder.mClickForMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExpanded){
                    mExpanded = false;
                    holder.mContentTextView.setMaxLines(3);
                    holder.mClickForMore.setText(mContext.getString(R.string.review_read_more));
                }else {
                    mExpanded = true;
                    holder.mContentTextView.setMaxLines(Integer.MAX_VALUE);
                    holder.mClickForMore.setText(mContext.getString(R.string.review_read_less));
                }
            }
        });
         holder.bind(position);




    }

    @Override
    public int getItemCount() {
        return mReviewsArray.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView mAuthorTextView;
        TextView mContentTextView;
        TextView mClickForMore;


        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            this.mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_author);
            this.mContentTextView = (TextView) itemView.findViewById(R.id.tv_content);
            this.mClickForMore = (TextView) itemView.findViewById(R.id.tv_click_to_read_more);
        }
        void bind(int pos){
            String authorString = mReviewsArray.get(pos).getAuthor();
            String contentString = mReviewsArray.get(pos).getContent();
            mAuthorTextView.setText(authorString);
            mContentTextView.setText(contentString);
            mContentTextView.post(new Runnable() {
                @Override
                public void run() {
                    int linecount = mContentTextView.getLineCount();
                    if(linecount > 3){
                        mClickForMore.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }
}
