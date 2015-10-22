package it.quip.android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.quip.android.R;
import it.quip.android.fragment.QuipFeedFragment;


public class QuipsViewHolder extends RecyclerView.ViewHolder {

    private QuipFeedFragment mFragment;
    public Context mContext;
    public LinearLayout mQuipProfile;
    public ImageView mIvProfile;
    public TextView mTvUserName;
    public TextView mTvQuipTimestamp;
    public TextView mTvQuipBody;
    public TextView mTvQuipSourceUserName;

    public QuipsViewHolder(Context context, View itemView, QuipFeedFragment fragment) {
        super(itemView);
        this.mFragment = fragment;
        this.mContext = context;
        this.mQuipProfile = (LinearLayout) itemView.findViewById(R.id.quip_profile);
        this.mIvProfile = (ImageView) this.mQuipProfile.findViewById(R.id.iv_profile);
        this.mTvUserName = (TextView) this.mQuipProfile.findViewById(R.id.tv_user_name);
        this.mTvQuipTimestamp = (TextView) itemView.findViewById(R.id.tv_quip_timestamp);
        this.mTvQuipBody = (TextView) itemView.findViewById(R.id.tv_quip_body);
        this.mTvQuipSourceUserName = (TextView) itemView.findViewById(R.id.tv_quip_source_user_name);
    }
}
