package it.quip.android.fragment;

import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import it.quip.android.model.Circle;
import it.quip.android.model.Quip;

public class CircleFeedFragment extends QuipFeedFragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private Circle mCircle;

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

}
