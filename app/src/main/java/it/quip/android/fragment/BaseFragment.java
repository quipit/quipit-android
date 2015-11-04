package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import it.quip.android.R;

abstract public class BaseFragment extends Fragment {

    protected ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        grabProgressBar();
        return super.onCreateView(inflater, container, savedInstanceState);
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
