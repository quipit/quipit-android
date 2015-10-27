package it.quip.android.fragment;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.adapter.CirclesArrayAdapter;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.model.Circle;


public class QuipSelectFragment extends SearchListFragment<Circle> {

    public static QuipSelectFragment newInstance() {
        return new QuipSelectFragment();
    }

    protected SearchArrayAdapter<Circle> getFilterAdapter(List<Circle> filteredValues) {
        return new CirclesArrayAdapter(getContext(), filteredValues);
    }

    protected SearchArrayAdapter<Circle> getSelectAdapter(List<Circle> selectedValues) {
        return new CirclesArrayAdapter(getContext(), selectedValues);
    }

    protected List<Circle> searchFor(String query) {
        List<Circle> circles = new ArrayList<>();
        for (Circle circle : QuipitApplication.getCurrentUser().getCircles()) {
            if (!"".equals(query) &&
                    circle.getName().toLowerCase().contains(query) &&
                    !alreadySelected(circle)) {
                circles.add(circle);
            }
        }

        return circles;
    }

}
