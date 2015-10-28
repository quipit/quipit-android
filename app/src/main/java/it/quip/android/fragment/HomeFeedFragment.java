package it.quip.android.fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import it.quip.android.model.Quip;

public class HomeFeedFragment extends QuipFeedFragment {

    @Override
    public void loadInitialQuips() {
        // TODO: should be scoped to users circles/friends
        new ParseQuery<>(Quip.class)
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
