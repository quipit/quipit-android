package it.quip.android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.quip.android.R;


public class ListSearchHolder extends SearchHolder {

    public ListSearchHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.layout = itemView;
        this.ivProfile = (ImageView) itemView.findViewById(R.id.iv_search_pic);
        this.tvName = (TextView) itemView.findViewById(R.id.tv_search_name);
        this.ivChecked = (ImageView) itemView.findViewById(R.id.iv_search_checked);
    }
}
