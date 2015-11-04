package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import it.quip.android.R;

abstract public class BaseFragment extends Fragment {

    public abstract CharSequence getTitle();

    protected ProgressBar mProgressBar;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        grabProgressBar();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public String stringRes(int resId) {
        return getResources().getString(resId);
    }

    public void grabProgressBar() {
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.pbProgressAction);
    }

    public void showProcessIndicators() {
        // This will show the network indicator
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressIndicators() {
        // TODO: Hide the network indicator here
        mProgressBar.setVisibility(View.GONE);
    }
}
