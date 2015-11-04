package it.quip.android.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

    private OnActionRequestedListener onActionRequestedListener;

    private SwipeRefreshLayout mSrlFeed;
    private RecyclerView mRvFeed;
    private LinearLayoutManager mLlManager;

    public abstract void loadInitialQuips();

    @Override
    public void onResume() {
        super.onResume();
        showProcessIndicators();
        loadInitialQuips();
    }

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

    public OnActionRequestedListener getOnActionRequestedListener() {
        return onActionRequestedListener;
    }

    public void setOnActionRequestedListener(OnActionRequestedListener onActionRequestedListener) {
        this.onActionRequestedListener = onActionRequestedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuips = new ArrayList<>();
        mQuipsAdapter = new QuipsAdapter(mQuips, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        setupViewDependencies(view);
        showProcessIndicators();
        loadInitialQuips();
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
                showProcessIndicators();
                loadMoreQuips(currentPage);
            }
        });
    }

    @Override
    public void showProcessIndicators() {
        // This will show the network indicator
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressIndicators() {
        mSrlFeed.setRefreshing(false);
        // TODO: Hide the network indicator here
        mProgressBar.setVisibility(View.GONE);
    }

}
