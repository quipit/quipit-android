package it.quip.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;


public abstract class SearchArrayAdapter <T extends ParseObject> extends RecyclerView.Adapter<SearchHolder> {

    private OnClickListener onClickListener;
    private List<T> mValues;
    private Set<String> mSelected;
    private List<String> mPreselectedValues;

    protected abstract String getName(T value);

    protected abstract String getImageUrl(T value);

    protected abstract SearchHolder getViewHolder(LayoutInflater inflater, ViewGroup parent);

    public interface OnClickListener <T extends ParseObject> {
        void onClick(int position, T value);
    }

    public SearchArrayAdapter(List<T> values, List<String> preselectedValues) {
        mValues = values;
        mPreselectedValues = preselectedValues;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        mSelected = new HashSet<String>();

        return getViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(final SearchHolder viewHolder, final int position) {
        T value = mValues.get(position);
        viewHolder.tvName.setText(getName(value));
        Picasso.with(viewHolder.context).load(getImageUrl(value)).transform(new CircleTransformation(4, Color.WHITE)).into(viewHolder.ivProfile);
        viewHolder.ivChecked.setImageDrawable(ContextCompat.getDrawable(viewHolder.context, R.drawable.ic_checked));
        viewHolder.ivChecked.setColorFilter(ContextCompat.getColor(viewHolder.context, R.color.quipit_checked));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    T value = mValues.get(position);
                    toggleChecked(viewHolder, value);
                    onClickListener.onClick(position, value);
                }
            }
        });

        setChecked(viewHolder, value);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void setChecked(SearchHolder viewHolder, T value) {
        String uid = value.getObjectId();
        if (mSelected.contains(uid) || isPreselected(value)) {
            showChecked(viewHolder.ivChecked);
        } else {
            hideChecked(viewHolder.ivChecked);
        }
    }

    private void toggleChecked(SearchHolder viewHolder, T value) {
        String uid = value.getObjectId();
        if (mSelected.contains(uid)) {
            hideChecked(viewHolder.ivChecked);
            mSelected.remove(uid);
        } else {
            showChecked(viewHolder.ivChecked);
            mSelected.add(uid);
        }
    }

    private void showChecked(ImageView iv) {
        iv.setVisibility(View.VISIBLE);
    }

    private void hideChecked(ImageView iv) {
        iv.setVisibility(View.INVISIBLE);
    }

    private boolean isPreselected(T value) {
        return (null != mPreselectedValues) && (mPreselectedValues.contains(value.getObjectId()));
    }

}