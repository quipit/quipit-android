package it.quip.android.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.quip.android.R;
import it.quip.android.fragment.QuipFeedFragment;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.Quip;
import it.quip.android.model.User;
import it.quip.android.util.FormatUtils;

public class QuipsAdapter extends RecyclerView.Adapter<QuipsViewHolder> {

    private List<Quip> mQuips;
    private QuipFeedFragment mFragment;

    public QuipsAdapter(List<Quip> quips, QuipFeedFragment fragment) {
        this.mQuips = quips;
        this.mFragment = fragment;
    }

    @Override
    public QuipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.item_quip, parent, false);
        return new QuipsViewHolder(parent.getContext(), contactView, mFragment);
    }

    @Override
    public void onBindViewHolder(QuipsViewHolder viewHolder, int position) {
        setupViews(mQuips.get(position), viewHolder);
    }

    @Override
    public int getItemCount() {
        return this.mQuips.size();
    }

    private void setupProfile(Quip quip, QuipsViewHolder viewHolder) {
        try {
            Picasso.with(viewHolder.mContext)
                    .load(quip.getAuthor().getImageUrl())
                    .fit()
                    .centerInside()
                    .transform(new CircleTransformation())
                    .into(viewHolder.mIvProfile);

        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("TweetsAdapter", "Unable to load: " + quip.getObjectId());
        }

        viewHolder.mTvUserName.setText(quip.getAuthor().getName());
    }

    private void setupViews(Quip quip, QuipsViewHolder viewHolder) {
        setupProfile(quip, viewHolder);
        viewHolder.mTvQuipTimestamp.setText(FormatUtils.getLongRelativeTimeAgo(quip.getTimestamp()));
        viewHolder.mTvQuipBody.setText(quip.getText());

        User source = quip.getSource();
        if (null == source) {
            viewHolder.mTvQuipSourceUserName.setText("");
            viewHolder.mQuipSourceIndicator.setVisibility(View.GONE);
        } else {
            viewHolder.mTvQuipSourceUserName.setText(source.getName());
            viewHolder.mQuipSourceIndicator.setVisibility(View.VISIBLE);
        }
    }

}
