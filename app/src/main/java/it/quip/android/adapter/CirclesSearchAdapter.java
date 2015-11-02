package it.quip.android.adapter;

import android.content.Context;

import java.util.List;

import it.quip.android.model.Circle;


public class CirclesSearchAdapter extends SearchArrayAdapter<Circle> {

    public CirclesSearchAdapter(Context context, List<Circle> objects) {
        super(context, objects);
    }

    protected String getSearchName(Circle circle) {
        return circle.getName();
    }

}
