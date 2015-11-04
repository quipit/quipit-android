package it.quip.android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.quip.android.R;

public class SearchHolder extends RecyclerView.ViewHolder {

    public Context context;
    public View layout;
    public ImageView ivProfile;
    public TextView tvName;
    public ImageView ivChecked;
    public boolean isChecked;

    public SearchHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.layout = itemView;
        this.ivProfile = (ImageView) itemView.findViewById(R.id.iv_search_pic);
        this.tvName = (TextView) itemView.findViewById(R.id.tv_search_name);
        this.ivChecked = (ImageView) itemView.findViewById(R.id.iv_search_checked);
        this.isChecked = false;
    }
}
