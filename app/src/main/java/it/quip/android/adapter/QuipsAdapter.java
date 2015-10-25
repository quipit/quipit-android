package it.quip.android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.quip.android.R;
import it.quip.android.fragment.QuipFeedFragment;
import it.quip.android.graphics.RoundedRectTransform;
import it.quip.android.model.Quip;

public class QuipsAdapter extends RecyclerView.Adapter<QuipsViewHolder> {

    private List<Quip> mQuips;
    private QuipFeedFragment mFragment;

    public QuipsAdapter(List<Quip> quips, QuipFeedFragment fragment) {
        this.mQuips = quips;
        this.mFragment = fragment;
    }

    @Override
    public QuipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_quip, parent, false);

        QuipsViewHolder viewHolder = new QuipsViewHolder(parent.getContext(), contactView, mFragment);
        return viewHolder;
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
                    .load("drawable/edgarjuarez.jpeg")
                    .transform(new RoundedRectTransform(10, 0))
                    .into(viewHolder.mIvProfile);

        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("TweetsAdapter", "Unable to load: " + quip.getUid());
        }

        viewHolder.mTvUserName.setText(quip.getAuthor().getName());
    }

    private void setupViews(Quip quip, QuipsViewHolder viewHolder) {
        setupProfile(quip, viewHolder);
        viewHolder.mTvQuipTimestamp.setText("2d");
        viewHolder.mTvQuipBody.setText(quip.getText());
        viewHolder.mTvQuipSourceUserName.setText(quip.getSource().getName());
    }

}
