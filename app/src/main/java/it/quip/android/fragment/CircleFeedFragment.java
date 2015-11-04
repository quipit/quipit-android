package it.quip.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import it.quip.android.R;
import it.quip.android.model.Circle;
import it.quip.android.model.Quip;

public class CircleFeedFragment extends QuipFeedFragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private Circle mCircle;

    private FloatingActionButton mActionButton;

    public static CircleFeedFragment newInstance(Circle circle) {
        CircleFeedFragment fragment = new CircleFeedFragment();
        Bundle args = new Bundle();
        args.putParcelable(CIRCLE, circle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCircle = getArguments().getParcelable(CIRCLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

        if (v != null) {
            mActionButton = (FloatingActionButton) inflater.inflate(
                    R.layout.fab_create_quip, container, false);

            setupActionButton();
            v.addView(mActionButton);
        }

        return v;
    }

    private void setupActionButton() {

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionButton.toggle(true);
                OnActionRequestedListener listener = getOnActionRequestedListener();
                if (null != listener) {
                    listener.onCreateQuipInCircle(mCircle);
                }
            }
        });
    }

    @Override
    public void loadInitialQuips() {
        new ParseQuery<>(Quip.class)
                .whereEqualTo(Quip.CIRCLE, mCircle)
                .addDescendingOrder("createdAt")
                .findInBackground(new FindCallback<Quip>() {
                    @Override
                    public void done(List<Quip> quips, ParseException e) {
                        setQuips(quips);
                        hideProgressIndicators();
                    }
                });
    }

    @Override
    public void loadMoreQuips(int page) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mActionButton.setVisibility(View.VISIBLE);
    }
}
