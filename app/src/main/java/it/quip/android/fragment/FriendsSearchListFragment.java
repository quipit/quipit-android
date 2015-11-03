package it.quip.android.fragment;

import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.adapter.UsersArrayAdapter;
import it.quip.android.model.User;
import it.quip.android.repository.user.UsersResponseHandler;

public class FriendsSearchListFragment extends SearchFragment<User> {

    public static FriendsSearchListFragment newInstance() {
        return new FriendsSearchListFragment();
    }

    public FriendsSearchListFragment() {
        super();
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void loadSearchValues() {
        QuipitApplication.getUserRepo().getFriends(QuipitApplication.getCurrentUser(), new UsersResponseHandler() {
            @Override
            public void onSuccess(List<User> users) {
                setSearchValues(users);
            }
        });
    }

    protected SearchArrayAdapter<User> getAdapter(List<User> filteredFriends) {
        return new UsersArrayAdapter(filteredFriends);
    }

    protected List<User> searchFor(String name) {
        List<User> users = new ArrayList<>();
        for (User friend : getSearchValues()) {
            if (!"".equals(name)
                    && !myself(friend)
                    && hasFriendWithName(friend, name)) {
                users.add(friend);
            }
        }

        return users;
    }

    protected Integer getMaxSelectCount() {
        return null;
    }

    private boolean hasFriendWithName(User friend, String name) {
        return friend.getName().toLowerCase().contains(name.toLowerCase());
    }

    private boolean myself(User friend) {
        return QuipitApplication.getCurrentUser().getName().equals(friend.getName());
    }

}
