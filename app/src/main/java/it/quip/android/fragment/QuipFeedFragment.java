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

public abstract class QuipFeedFragment extends BaseFragment {

    @Override
    public CharSequence getTitle() {
        return "Quips";
    }

    private List<Quip> mQuips;
    private QuipsAdapter mQuipsAdapter;

    private SwipeRefreshLayout mSrlFeed;
    private RecyclerView mRvFeed;
    private LinearLayoutManager mLlManager;

    public abstract void loadInitialQuips();
    public abstract void loadMoreQuips(int page);

    public void addQuip(Quip quip) {
        mQuips.add(quip);
        mQuipsAdapter.notifyDataSetChanged();
    }

    public void addQuips(List<Quip> quips) {
        mQuips.addAll(quips);
        mQuipsAdapter.notifyDataSetChanged();
    }

    public void setQuips(List<Quip> quips) {
        mQuips.clear();
        addQuips(quips);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuips = new ArrayList<>();
        mQuipsAdapter = new QuipsAdapter(mQuips, this);

        loadInitialQuips();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        setupViewDependencies(view);
        return view;
    }

    private void setupViewDependencies(View view) {
        mSrlFeed = (SwipeRefreshLayout) view.findViewById(R.id.srlFeed);
        mSrlFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInitialQuips();
            }
        });

        mLlManager = new LinearLayoutManager(getContext());

        mRvFeed = (RecyclerView) view.findViewById(R.id.rv_feed);
        mRvFeed.setAdapter(mQuipsAdapter);
        mRvFeed.setLayoutManager(mLlManager);
        mRvFeed.addOnScrollListener(new EndlessScrollListener(mLlManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreQuips(currentPage);
            }
        });
    }

    public void showProcessIndicators() {
        // This will show the network indicator
    }

    public void hideProgressIndicators() {
        mSrlFeed.setRefreshing(false);
        // TODO: Hide the network indicator here
    }

}
