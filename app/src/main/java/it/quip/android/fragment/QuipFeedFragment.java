package it.quip.android.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.R;
import it.quip.android.adapter.QuipsAdapter;
import it.quip.android.listener.EndlessScrollListener;
import it.quip.android.model.Quip;

public class QuipFeedFragment extends BaseFragment {

    @Override
    public CharSequence getTitle() {
        return "Quips";
    }

    protected List<Quip> mQuips;
    protected QuipsAdapter mQuipsAdapter;

    private SwipeRefreshLayout mSrlFeed;
    private RecyclerView mRvFeed;
    private LinearLayoutManager mLlManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        setupViewDependencies(view);
        populateFeed(false);

        return view;
    }

    private void setupViewDependencies(View view) {
        mSrlFeed = (SwipeRefreshLayout) view.findViewById(R.id.srlFeed);
        mSrlFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateFeed(false);
            }
        });

        mRvFeed = (RecyclerView) view.findViewById(R.id.rv_feed);
        mQuips = new ArrayList<Quip>();
        mQuipsAdapter = new QuipsAdapter(mQuips, this);
        mRvFeed.setAdapter(mQuipsAdapter);

        mLlManager = new LinearLayoutManager(getContext());
        mRvFeed.setLayoutManager(mLlManager);
        mRvFeed.addOnScrollListener(new EndlessScrollListener(mLlManager) {

            @Override
            public void onLoadMore(int currentPage) {
                populateFeed(true);
            }

        });
    }

    private void populateFeed(final boolean additional) {
        if (mQuips.size() < 20) {
            if (!additional) {
                mQuips.clear();
            }

            //mQuips.addAll(MockUtils.getQuips());
            mQuipsAdapter.notifyDataSetChanged();
        }

        mSrlFeed.setRefreshing(false);
    }

}
