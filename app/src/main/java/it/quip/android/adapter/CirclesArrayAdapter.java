package it.quip.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.quip.android.R;
import it.quip.android.model.Circle;


public class CirclesArrayAdapter extends SearchArrayAdapter<Circle, GridSearchHolder> {

    public CirclesArrayAdapter(List<Circle> values) {
        super(values);
    }

    protected String getName(Circle circle) {
        return circle.getName();
    }

    protected String getImageUrl(Circle circle) {
        return circle.getAvatarImageURL();
    }

    protected GridSearchHolder getViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View contactView = inflater.inflate(R.layout.item_circle, parent, false);
        return new GridSearchHolder(parent.getContext(), contactView);
    }

}
