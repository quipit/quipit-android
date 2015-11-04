package it.quip.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.quip.android.R;
import it.quip.android.activity.QuipitHomeActivity;
import it.quip.android.model.Circle;

public class ViewCircleFragment extends BaseFragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private Circle circle;

    public static ViewCircleFragment newInstance(Circle circle) {
        ViewCircleFragment fragment = new ViewCircleFragment();
        Bundle args = new Bundle();
        args.putParcelable(CIRCLE, circle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getTitle() {
        return circle.getName();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circle = getArguments().getParcelable(CIRCLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_circle, container, false);
        setupFragments();
        return v;
    }

    private void setupFragments() {
        CircleHeaderFragment circleHeader = CircleHeaderFragment.newInstance(circle);
        CircleFeedFragment circleFeed = CircleFeedFragment.newInstance(circle);
        circleFeed.setOnActionRequestedListener(((QuipitHomeActivity) getContext()).getOnActionRequestedListener());

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_header, circleHeader)
                .replace(R.id.fl_quips, circleFeed)
                .commit();
    }


}
