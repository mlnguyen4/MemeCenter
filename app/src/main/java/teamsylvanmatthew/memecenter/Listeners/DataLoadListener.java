package teamsylvanmatthew.memecenter.Listeners;

import android.widget.AbsListView;

public abstract class DataLoadListener implements AbsListView.OnScrollListener {
    public boolean loading;
    private int previousTotalItemCount = 0;

    public DataLoadListener() {
        loading = true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //System.out.println("scroll, loading: " + loading + ", firstVisibleItem: " + firstVisibleItem + ", visibleItemCount: " + visibleItemCount + ", totalItemCount: " + totalItemCount);

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (firstVisibleItem + visibleItemCount) >= totalItemCount) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
