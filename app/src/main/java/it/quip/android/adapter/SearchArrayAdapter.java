package it.quip.android.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;


public abstract class SearchArrayAdapter <T extends ParseObject> extends RecyclerView.Adapter<SearchHolder> {

    public final static int CHECKED_ANIMATION_DURATION = 200;

    private OnClickListener onClickListener;
    private List<T> mValues;
    private Set<String> mSelected;
    private List<String> mPreselectedValues;

    protected abstract String getName(T value);

    protected abstract String getImageUrl(T value);

    protected abstract SearchHolder getViewHolder(LayoutInflater inflater, ViewGroup parent);

    public interface OnClickListener <T extends ParseObject> {
        void onClick(int position, T value);
    }

    public SearchArrayAdapter(List<T> values, List<String> preselectedValues) {
        mValues = values;
        mPreselectedValues = preselectedValues;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        mSelected = new HashSet<>();

        return getViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(final SearchHolder viewHolder, final int position) {
        T value = mValues.get(position);
        viewHolder.tvName.setText(getName(value));
        Picasso.with(viewHolder.context).load(getImageUrl(value)).transform(new CircleTransformation(4, Color.WHITE)).into(viewHolder.ivProfile);
        viewHolder.ivChecked.setImageDrawable(ContextCompat.getDrawable(viewHolder.context, R.drawable.ic_checked));
        viewHolder.ivChecked.setColorFilter(ContextCompat.getColor(viewHolder.context, R.color.quipit_checked));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    T value = mValues.get(position);
                    toggleChecked(viewHolder, value);
                    onClickListener.onClick(position, value);
                }
            }
        });

        setChecked(viewHolder, value);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void setChecked(SearchHolder viewHolder, T value) {
        String uid = value.getObjectId();
        if (mSelected.contains(uid) || isPreselected(value)) {
            viewHolder.ivChecked.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivChecked.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleChecked(SearchHolder viewHolder, T value) {
        String uid = value.getObjectId();
        if (mSelected.contains(uid)) {
            hideChecked(viewHolder.ivChecked);
            mSelected.remove(uid);
        } else {
            showChecked(viewHolder.ivChecked);
            mSelected.add(uid);
        }
    }

    private void showChecked(final ImageView iv) {
        ObjectAnimator appear = ObjectAnimator.ofFloat(iv, View.ALPHA, 0, 1);
        ObjectAnimator zoomInX = ObjectAnimator.ofFloat(iv, View.SCALE_X, 1.3f, 1.0f);
        ObjectAnimator zoomInY = ObjectAnimator.ofFloat(iv, View.SCALE_Y, 1.3f, 1.0f);

        AnimatorSet zoomOutDisappear = new AnimatorSet();
        zoomOutDisappear.setDuration(CHECKED_ANIMATION_DURATION);
        zoomOutDisappear.setInterpolator(new AccelerateInterpolator());
        zoomOutDisappear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                iv.setVisibility(View.VISIBLE);
            }
        });

        zoomOutDisappear.play(appear).with(zoomInX).with(zoomInY);
        zoomOutDisappear.start();
    }

    private boolean isPreselected(T value) {
        return (null != mPreselectedValues) && (mPreselectedValues.contains(value.getObjectId()));
    }

    private void hideChecked(final ImageView iv) {
        ObjectAnimator disappear = ObjectAnimator.ofFloat(iv, View.ALPHA, 1, 0);
        ObjectAnimator zoomOutX = ObjectAnimator.ofFloat(iv, View.SCALE_X, 1.0f, 1.3f);
        ObjectAnimator zoomOutY = ObjectAnimator.ofFloat(iv, View.SCALE_Y, 1.0f, 1.3f);

        AnimatorSet zoomOutDisappear = new AnimatorSet();
        zoomOutDisappear.setDuration(CHECKED_ANIMATION_DURATION);
        zoomOutDisappear.setInterpolator(new DecelerateInterpolator());
        zoomOutDisappear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                iv.setVisibility(View.INVISIBLE);
            }
        });

        zoomOutDisappear.play(disappear).with(zoomOutX).with(zoomOutY);
        zoomOutDisappear.start();
    }
}