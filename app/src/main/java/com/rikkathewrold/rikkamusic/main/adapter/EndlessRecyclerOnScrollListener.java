package com.rikkathewrold.rikkamusic.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rikkathewrold.rikkamusic.util.LogUtil;

/**
 * Adapter的懒加载
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScroll";

    //总加载数据
    public int previousTotal = 0;
    //是否提前加载
    private boolean loading = true;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int currentPage = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    int type;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
        type = 1;
    }

    public EndlessRecyclerOnScrollListener(GridLayoutManager gridLayoutManager) {
        mGridLayoutManager = gridLayoutManager;
        type = 2;
    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        LogUtil.d(TAG,"onScrolled");
        super.onScrolled(recyclerView, dx, dy);
        if (type == 1) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        } else if (type == 2) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mGridLayoutManager.getItemCount();
            firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
        }
        LogUtil.d(TAG, "totalItemCount :" + totalItemCount + " visibleItemCount :" + visibleItemCount + " firstVisibleItem : " + firstVisibleItem);

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            LogUtil.d(TAG, "onLoadMore");
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);

    public void reset(int previousTotal, boolean loading) {
        this.previousTotal = previousTotal;
        this.loading = loading;
    }
}
