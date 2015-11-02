package it.quip.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.Circle;

public class CirclesAdapter extends ArrayAdapter<Circle> {

    public CirclesAdapter(Context context, List<Circle> circles) {
        super(context, 0, circles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Circle circle = getItem(position);
        ViewHolder holder;

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_circle, parent, false);

            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(circle.getName());

        Picasso.with(getContext())
                .load(circle.getAvatarImageURL())
                .transform(new CircleTransformation())
                .fit()
                .centerCrop()
                .into(holder.ivAvatar);

        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public ImageView ivAvatar;
    }

}
