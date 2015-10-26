package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.R;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.view.OverlayListView;


public abstract class SearchListFragment<T extends ParseObject> extends Fragment {

    public interface OnSearchListChangedListener <T extends ParseObject> {
        void onSelect(T object);
        void onUnselect(T object);
    }

    private EditText mEtSearch;

    private SearchArrayAdapter<T> mFilteredValues;

    private ArrayList<T> mValues;
    private SearchArrayAdapter<T> mSelectedValues;

    private OnSearchListChangedListener onSearchListChangedListener;

    public List<T> getSelected() {
        return mValues;
    }

    public void setOnSearchListChangedListener(OnSearchListChangedListener onSearchListChangedListener) {
        this.onSearchListChangedListener = onSearchListChangedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<T> filteredValues = new ArrayList<>();
        mFilteredValues = getFilterAdapter(filteredValues);

        mValues = new ArrayList<>();
        mSelectedValues = getSelectAdapter(mValues);

        mSelectedValues.setOnLongClickListener(new SearchArrayAdapter.OnLongClickListener<T>() {
            @Override
            public boolean onLongClick(int position, T value) {
                unselectValue(value);
                return true;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_searchable_list, container, false);

        mEtSearch = (EditText) v.findViewById(R.id.et_search);

        OverlayListView valuesOverlay = (OverlayListView) v.findViewById(R.id.overlay_values);
        valuesOverlay.setAdapter(mFilteredValues);
        valuesOverlay.setOnItemSelectedListener(new OverlayListView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position, View view) {
                selectValue(mFilteredValues.getItem(position));
            }
        });

        ListView lvSelectedValues = (ListView) v.findViewById(R.id.lv_selected_values);
        lvSelectedValues.setAdapter(mSelectedValues);

        setupSearchField();
        return v;
    }

    private void selectValue(T value) {
        mSelectedValues.add(value);

        mEtSearch.setText("");
        mFilteredValues.clear();

        if (onSearchListChangedListener != null) {
            onSearchListChangedListener.onSelect(value);
        }
    }

    private void unselectValue(T value) {
        mSelectedValues.remove(value);
        if (onSearchListChangedListener != null) {
            onSearchListChangedListener.onUnselect(value);
        }
    }

    private void setupSearchField() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void search(String name) {
        mFilteredValues.clear();
        mFilteredValues.addAll(searchFor(name));
    }

    protected boolean alreadySelected(T value) {
        return mValues.contains(value);
    }

    protected abstract SearchArrayAdapter<T> getFilterAdapter(List<T> filteredValues);

    protected abstract SearchArrayAdapter<T> getSelectAdapter(List<T> selectedValues);

    protected abstract List<T> searchFor(String query);

}
