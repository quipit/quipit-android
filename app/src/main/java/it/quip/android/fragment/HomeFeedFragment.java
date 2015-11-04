package it.quip.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import it.quip.android.R;
import it.quip.android.model.Quip;

public class HomeFeedFragment extends QuipFeedFragment {

    private FloatingActionMenu mActionMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

        if (v != null) {
            mActionMenu = (FloatingActionMenu) inflater.inflate(
                    R.layout.fab_create_options, container, false);

            setupActionMenu();
            v.addView(mActionMenu);
        }

        return v;
    }

    private void setupActionMenu() {
        FloatingActionButton fabCircle = (FloatingActionButton) mActionMenu.findViewById(R.id.fab_circle_new);
        fabCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionMenu.toggle(true);
                OnActionRequestedListener listener = getOnActionRequestedListener();
                if (listener != null) {
                    listener.onCreateCircle();
                }
            }
        });

        FloatingActionButton fabQuip = (FloatingActionButton) mActionMenu.findViewById(R.id.fab_quip_new);
        fabQuip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionMenu.toggle(true);
                OnActionRequestedListener listener = getOnActionRequestedListener();
                if (listener != null) {
                    listener.onCreateQuip();
                }
            }
        });
    }

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
