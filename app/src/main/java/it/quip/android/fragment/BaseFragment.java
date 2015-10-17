package it.quip.android.fragment;

import android.support.v4.app.Fragment;

abstract public class BaseFragment extends Fragment {

    public abstract CharSequence getTitle();

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());
    }
}
