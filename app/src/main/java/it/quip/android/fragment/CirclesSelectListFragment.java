package it.quip.android.fragment;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.adapter.CirclesSearchAdapter;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.model.Circle;


public class CirclesSelectListFragment extends SearchListFragment<Circle> {

    public static CirclesSelectListFragment newInstance() {
        return new CirclesSelectListFragment();
    }

    protected void loadSearchValues() {
        setSearchValues(QuipitApplication.getCurrentUser().getCircles());
    }

    protected SearchArrayAdapter<Circle> getFilterAdapter(List<Circle> filteredValues) {
        return new CirclesSearchAdapter(getContext(), filteredValues);
    }

    protected SearchArrayAdapter<Circle> getSelectAdapter(List<Circle> selectedValues) {
        return new CirclesSearchAdapter(getContext(), selectedValues);
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
