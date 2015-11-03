package it.quip.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.quip.android.R;


public abstract class SearchArrayAdapter <T extends ParseObject> extends RecyclerView.Adapter<SearchHolder> {

    private OnClickListener onClickListener;
    private List<T> mValues;
    private Set<String> mSelected;

    protected abstract String getName(T value);

    protected abstract String getImageUrl(T value);

    protected abstract SearchHolder getViewHolder(LayoutInflater inflater, ViewGroup parent);

    public interface OnClickListener <T extends ParseObject> {
        void onClick(int position, T value);
    }

    public SearchArrayAdapter(List<T> values) {
        mValues = values;
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
        Picasso.with(viewHolder.context).load(getImageUrl(value)).into(viewHolder.ivProfile);

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
        if (mSelected.contains(uid)) {
            Picasso.with(viewHolder.context).load(R.drawable.ic_checked).into(viewHolder.ivChecked);
        } else {
            Picasso.with(viewHolder.context).load(R.drawable.ic_unchecked).into(viewHolder.ivChecked);
        }
    }

    private void toggleChecked(SearchHolder viewHolder, T value) {
        String uid = value.getObjectId();
        if (mSelected.contains(uid)) {
            Picasso.with(viewHolder.context).load(R.drawable.ic_unchecked).into(viewHolder.ivChecked);
            mSelected.remove(uid);
        } else {
            Picasso.with(viewHolder.context).load(R.drawable.ic_checked).into(viewHolder.ivChecked);
            mSelected.add(uid);
        }
    }

}