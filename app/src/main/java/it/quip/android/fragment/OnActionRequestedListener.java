package it.quip.android.fragment;

import it.quip.android.model.Circle;

public abstract class OnActionRequestedListener {
    public abstract void onCreateQuip();
    public abstract void onCreateCircle();
    public abstract void onCreateQuipInCircle(Circle circle);
}
