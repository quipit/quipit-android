package it.quip.android.fragment;

import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.adapter.CirclesArrayAdapter;
import it.quip.android.adapter.GridSearchHolder;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.model.Circle;


public class CirclesSearchGridFragment extends SearchFragment<Circle, GridSearchHolder> {

    private static final int GRID_WIDTH = 3;

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

    protected SearchArrayAdapter<Circle, GridSearchHolder> getAdapter(List<Circle> circles) {
        return new CirclesArrayAdapter(circles);
    }

    protected List<Circle> searchFor(String query) {
        List<Circle> circles = new ArrayList<>();
        for (Circle circle : getSearchValues()) {
            if (!"".equals(query) &&
                    circle.getName().toLowerCase().contains(query) &&
                    !alreadySelected(circle)) {
                circles.add(circle);
            }
        }

        return circles;
    }

    protected Integer getMaxSelectCount() {
        return null;
    }

}
