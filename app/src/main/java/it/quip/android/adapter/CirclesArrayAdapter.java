package it.quip.android.adapter;

import java.util.List;

import it.quip.android.model.Circle;


public class CirclesArrayAdapter extends SearchArrayAdapter<Circle> {

    public CirclesArrayAdapter(List<Circle> values) {
        super(values);
    }

    protected String getName(Circle circle) {
        return circle.getName();
    }

    protected String getImageUrl(Circle circle) {
        return circle.getAvatarImageURL();
    }

}
