package com.mobitant.bestfood.custom;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * 리사이클러뷰에서 스크롤 할 경우, 데이터를 추가적으로 가지고 올 수 있도록 해주는 추상 클래스
 */
public abstract class EndlessRecyclerViewReverseScrollListener extends RecyclerView.OnScrollListener {
    // Sets the starting page index
    private static final int startingPageIndex = 0;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    private RecyclerView.LayoutManager mLayoutManager;
    private LoadOnScrollDirection mDirection;

    public EndlessRecyclerViewReverseScrollListener(LinearLayoutManager layoutManager, LoadOnScrollDirection direction) {
        this.mLayoutManager = layoutManager;
        mDirection = direction;
    }

    public EndlessRecyclerViewReverseScrollListener(GridLayoutManager layoutManager, LoadOnScrollDirection direction) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        mDirection = direction;
    }

    public EndlessRecyclerViewReverseScrollListener(StaggeredGridLayoutManager layoutManager, LoadOnScrollDirection direction) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        mDirection = direction;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int firstVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions =
                    ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            int[] firstVisibleItemPositions =
                    ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            firstVisibleItemPosition = getFirstVisibleItem(firstVisibleItemPositions);
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition =
                    ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition =
                    ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            firstVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        }

        switch (mDirection) {
            case BOTTOM:
                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = startingPageIndex;
                    this.previousTotalItemCount = totalItemCount;
                    if (totalItemCount == 0) {
                        this.loading = true;
                    }
                }
                // If it’s still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.
                if (loading && (totalItemCount > previousTotalItemCount)) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                }

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                    currentPage++;
                    onLoadMore(currentPage, totalItemCount);
                    loading = true;
                }
                break;
            case TOP:
                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = startingPageIndex;
                    this.previousTotalItemCount = totalItemCount;
                    if (totalItemCount == 0) {
                        this.loading = true;
                    }
                }
                // If it’s still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.
                if (loading && (totalItemCount > previousTotalItemCount)) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                }

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                if (!loading && firstVisibleItemPosition < visibleThreshold) {
                    currentPage++;
                    onLoadMore(currentPage, totalItemCount);
                    loading = true;
                }
                break;
        }
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    private int getFirstVisibleItem(int[] firstVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < firstVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = firstVisibleItemPositions[i];
            } else if (firstVisibleItemPositions[i] > maxSize) {
                maxSize = firstVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount);

    public enum LoadOnScrollDirection {
        TOP, BOTTOM
    }
}