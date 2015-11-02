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

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_circle, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(circle.getName());

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
        Picasso.with(getContext())
                .load(circle.getAvatarImageURL())
                .transform(new CircleTransformation())
                .fit()
                .centerCrop()
                .into(ivAvatar);

        return convertView;
    }

}
