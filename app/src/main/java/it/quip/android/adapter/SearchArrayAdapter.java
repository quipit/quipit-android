package it.quip.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import it.quip.android.R;


public abstract class SearchArrayAdapter <T extends ParseObject> extends ArrayAdapter<T> {

    private OnLongClickListener onLongClickListener;

    public SearchArrayAdapter(Context context, List<T> values) {
        super(context, 0, values);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        T value = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_user, parent, false);

            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(getSearchName(value));

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickListener != null) {
                    return onLongClickListener.onLongClick(position, getItem(position));
                }

                return false;
            }
        });

        return convertView;
    }

    public interface OnLongClickListener <T extends ParseObject> {
        boolean onLongClick(int position, T value);
    }

    public class ViewHolder {
        TextView tvName;
    }

    protected abstract String getSearchName(T value);

}