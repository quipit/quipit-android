package it.quip.android.fragment;

import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.adapter.CirclesSearchAdapter;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.model.Circle;


public class CirclesSearchGridFragment extends SearchFragment<Circle> {

    private static final int GRID_WIDTH = 2;

    public static CirclesSearchGridFragment newInstance() {
        return new CirclesSearchGridFragment();
    }

    public CirclesSearchGridFragment() {
        super();
        setLayoutManager(new GridLayoutManager(getContext(), GRID_WIDTH));
    }

    protected void loadSearchValues() {
        setSearchValues(QuipitApplication.getCurrentUser().getCircles());
    }

    protected SearchArrayAdapter<Circle> getAdapter(List<Circle> circles) {
        return new CirclesSearchAdapter(circles);
    }

    protected List<Circle> searchFor(String query) {
        List<Circle> circles = new ArrayList<>();
        for (Circle circle : getSearchValues()) {
            if (!"".equals(query) &&
                    circle.getName().toLowerCase().contains(query)) {
                circles.add(circle);
            }
        }

        return circles;
    }

    protected Integer getMaxSelectCount() {
        return null;
    }

}
