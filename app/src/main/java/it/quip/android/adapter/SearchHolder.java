package it.quip.android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class SearchHolder extends RecyclerView.ViewHolder {

    public Context context;
    public View layout;
    public ImageView ivProfile;
    public TextView tvName;
    public ImageView ivChecked;

    public SearchHolder(View itemView) {
        super(itemView);
    }
}
