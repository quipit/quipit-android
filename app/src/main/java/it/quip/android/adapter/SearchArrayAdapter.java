package it.quip.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;


public abstract class SearchArrayAdapter <T extends ParseObject, U extends SearchHolder> extends RecyclerView.Adapter<U> {

    private OnLongClickListener onLongClickListener;
    private OnClickListener onClickListener;
    private List<T> mValues;

    protected abstract String getName(T value);

    protected abstract String getImageUrl(T value);

    protected abstract U getViewHolder(LayoutInflater inflater, ViewGroup parent);

    public interface OnClickListener <T extends ParseObject> {
        void onClick(int position, T value);
    }

    public interface OnLongClickListener <T extends ParseObject> {
        boolean onLongClick(int position, T value);
    }

    public SearchArrayAdapter(List<T> values) {
        mValues = values;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public U onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        return getViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(U viewHolder, final int position) {
        T value = mValues.get(position);
        viewHolder.tvName.setText(getName(value));
        Picasso.with(viewHolder.context).load(getImageUrl(value)).into(viewHolder.ivProfile);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onClick(position, mValues.get(position));
                }
            }
        });

        viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickListener != null) {
                    return onLongClickListener.onLongClick(position, mValues.get(position));
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}