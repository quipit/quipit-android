package it.quip.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.quip.android.R;
import it.quip.android.model.User;

public class UsersArrayAdapter extends ArrayAdapter<User> {

    private OnLongClickListener onLongClickListener;

    public UsersArrayAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        User user = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_user, parent, false);

            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(user.getName());

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

    public interface OnLongClickListener {
        boolean onLongClick(int position, User user);
    }

    public class ViewHolder {
        TextView tvName;
    }

}
