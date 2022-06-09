package com.project45.ilovepadma.global;

import android.content.Context;
import android.widget.AbsListView;

/**
 * Created by azlan on 7/11/16.
 */
public abstract class Scroller implements AbsListView.OnScrollListener {
    private Context mContext;
    private int mCurrentFirstVisibleItem, mCurrentVisibleItemCount, mTotalItem;

    public Scroller(){
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        scrollCompleted(mCurrentFirstVisibleItem, mCurrentVisibleItemCount, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mCurrentFirstVisibleItem = firstVisibleItem;
        mCurrentVisibleItemCount = visibleItemCount;
    }

    public void scrollCompleted(int currentFirstVisibleItem, int currentVisibleItemCount, int scrollState){

    }
}
