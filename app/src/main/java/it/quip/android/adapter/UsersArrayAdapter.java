package it.quip.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.quip.android.R;
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

    protected SearchHolder getViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View contactView = inflater.inflate(R.layout.item_user, parent, false);
        return new SearchHolder(parent.getContext(), contactView);
    }

}
