package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.R;
import it.quip.android.adapter.SearchArrayAdapter;
import it.quip.android.adapter.SearchHolder;
import it.quip.android.model.BaseParseObject;


public abstract class SearchFragment <T extends BaseParseObject, U extends SearchHolder> extends Fragment {

    public interface OnSearchListChangedListener <T extends BaseParseObject> {
        void onSelect(T object);
        void onUnselect(T object);
    }

    private EditText mEtSearch;
    private RecyclerView mRvValues;

    private List<T> mValues;
    private List<T> mSelectedValues;
    private List<T> mFilteredValues;
    private SearchArrayAdapter<T, U> mFilteredAdapter;
    private OnSearchListChangedListener mOnSearchListChangedListener;
    private RecyclerView.LayoutManager mLayoutManager;

    public List<T> getSelectedValues() {
        return mSelectedValues;
    }

    public void setOnSearchListChangedListener(OnSearchListChangedListener mOnSearchListChangedListener) {
        this.mOnSearchListChangedListener = mOnSearchListChangedListener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectedValues = new ArrayList<>();
        mFilteredValues = new ArrayList<>();

        mFilteredAdapter = getAdapter(mFilteredValues);
        mFilteredAdapter.setOnClickListener(new SearchArrayAdapter.OnClickListener<T>() {
            @Override
            public void onClick(int position, T value) {
                selectValue(value);
            }
        });

        mFilteredAdapter.setOnLongClickListener(new SearchArrayAdapter.OnLongClickListener<T>() {
            @Override
            public boolean onLongClick(int position, T value) {
                unselectValue(value);
                return true;
            }
        });

        loadSearchValues();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_searchable_list, container, false);

        mEtSearch = (EditText) v.findViewById(R.id.et_search);
        setupSearchField();

        mRvValues = (RecyclerView) v.findViewById(R.id.rv_values);
        mRvValues.setAdapter(mFilteredAdapter);
        mRvValues.setLayoutManager(mLayoutManager);

        return v;
    }

    public void search(String name) {
        mFilteredValues.clear();
        if (name.isEmpty()) {
            mFilteredValues.addAll(mValues);
        } else {
            mFilteredValues.addAll(searchFor(name));
        }

        mFilteredAdapter.notifyDataSetChanged();
    }

    private void selectValue(T value) {
        Integer maxSelectCount = getMaxSelectCount();
        if ((null != maxSelectCount) && (mSelectedValues.size() >= maxSelectCount)) {
            mSelectedValues.remove(0);
        }

        mSelectedValues.add(value);
        //moveValueToTop(value);
        mEtSearch.setText("");

        if (mOnSearchListChangedListener != null) {
            mOnSearchListChangedListener.onSelect(value);
        }
    }

    private void moveValueToTop(T value) {
        int previousLocation = mValues.indexOf(value);
        mValues.remove(value);
        mValues.add(0, value);
        mFilteredAdapter.notifyItemMoved(previousLocation, 0);
    }

    private void unselectValue(T value) {
        mSelectedValues.remove(value);
        if (mOnSearchListChangedListener != null) {
            mOnSearchListChangedListener.onUnselect(value);
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

    protected void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    protected List<T> getSearchValues() {
        return mValues;
    }

    protected void setSearchValues(List<T> searchValues) {
        mValues = searchValues;
        mFilteredValues.addAll(mValues);
        mFilteredAdapter.notifyDataSetChanged();
    }

    protected boolean alreadySelected(T value) {
        return mSelectedValues.contains(value);
    }

    protected abstract void loadSearchValues();

    protected abstract SearchArrayAdapter<T, U> getAdapter(List<T> filteredValues);

    protected abstract List<T> searchFor(String query);

    protected abstract Integer getMaxSelectCount();

}
