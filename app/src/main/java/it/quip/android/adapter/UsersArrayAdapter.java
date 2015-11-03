package it.quip.android.adapter;

import java.util.List;

import it.quip.android.model.User;

public class UsersArrayAdapter extends SearchArrayAdapter<User> {

    public UsersArrayAdapter(List<User> values) {
        super(values);
    }

    protected String getName(User user) {
        return user.getName();
    }

    protected String getImageUrl(User user) {
        return user.getImageUrl();
    }

}
