package com.shahrir.nytimes_articles_search;


import android.widget.AbsListView;



//class that ensures continuous scroll
public abstract class nytimes_scrollops implements AbsListView.OnScrollListener {

    int visibleThreshold = 4;
    int currentPage = 0;
    int previousTotalItemCount = 0;
    int startingPageIndex = 0;
    boolean loading = true;

    // constructors as required
    public nytimes_scrollops() {
    }

    public nytimes_scrollops(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public nytimes_scrollops(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
            loading = onLoadMore(currentPage + 1, totalItemCount);
        }

        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
            loading = onLoadMore(currentPage + 1, totalItemCount);
        }

    }
}
