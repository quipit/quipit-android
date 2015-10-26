package it.quip.android.adapter;

import android.content.Context;

import java.util.List;

import it.quip.android.model.User;

public class UsersArrayAdapter extends SearchArrayAdapter<User> {

    public UsersArrayAdapter(Context context, List<User> objects) {
        super(context, objects);
    }

    protected String getSearchName(User user) {
        return user.getName();
    }

}
