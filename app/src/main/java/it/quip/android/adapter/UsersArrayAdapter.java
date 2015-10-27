package it.quip.android.adapter;

import android.content.Context;

import java.util.List;

import it.quip.android.model.User;

public class UsersArrayAdapter extends SearchArrayAdapter<User> {

    public UsersArrayAdapter(Context context, List<User> users) {
        super(context, users);
    }

    protected String getSearchName(User user) {
        return user.getName();
    }

}
