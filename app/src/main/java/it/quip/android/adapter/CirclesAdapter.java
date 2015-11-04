package it.quip.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.Circle;

public class CirclesAdapter extends RecyclerView.Adapter<CirclesAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(Circle circle, int position);
    }

    private Context mContext;
    private OnClickListener mListener;

    private List<Circle> mCircles;


    public CirclesAdapter(Context context, List<Circle> circles) {
        mContext = context;
        mCircles = circles;
    }

    public void setOnItemClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View circleView = inflater.inflate(R.layout.item_circle, parent, false);
        return new ViewHolder(circleView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Circle circle = mCircles.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });

        holder.tvName.setText(circle.getName());
        Picasso.with(mContext)
                .load(circle.getAvatarImageURL())
                .transform(new CircleTransformation())
                .fit()
                .centerCrop()
                .into(holder.ivAvatar);
    }

    private void onItemClick(int position) {
        if (mListener != null) {
            mListener.onClick(mCircles.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mCircles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView ivAvatar;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

}
