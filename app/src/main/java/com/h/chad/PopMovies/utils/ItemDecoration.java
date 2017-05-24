package com.h.chad.PopMovies.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chad on 5/10/2017.
 * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ItemDecoration.html
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public ItemDecoration(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
            RecyclerView parent, RecyclerView.State state){
        outRect.left = space;
        outRect.right = space;

        if(parent.getChildLayoutPosition(view) == 0){
            outRect.top = space;
        }
        else {
            outRect.top = space;
        }
    }
}
