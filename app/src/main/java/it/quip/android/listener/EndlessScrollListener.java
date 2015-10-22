package it.quip.android.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int mPreviousTotalItemCount = 0;
    private int mVisibleThreshold = 5;
    private int mCurrentPage = 0;

    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
    private boolean mLoading = true;

    private LinearLayoutManager mLayoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mLayoutManager.getItemCount();
        mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

        if (mLoading) {
            if (mTotalItemCount > mPreviousTotalItemCount) {
                mLoading = false;
                mPreviousTotalItemCount = mTotalItemCount;
            }
        }

        if (!mLoading && (mTotalItemCount - mVisibleItemCount)
                <= (mFirstVisibleItem + mVisibleThreshold)) {
            mCurrentPage++;
            onLoadMore(mCurrentPage);
            mLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);

}
