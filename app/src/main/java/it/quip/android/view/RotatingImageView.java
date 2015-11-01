package it.quip.android.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.quip.android.R;

public class RotatingImageView extends FrameLayout {

    private static final int DEFAULT_TRANSITION_DURATION = 1500;
    private static final int DEFAULT_TRANSITION_DELAY = 10000;

    private ImageView ivOne;
    private ImageView ivTwo;

    private ImageView ivCurrent;
    private ImageView ivNext;

    private Handler handler;

    private int[] drawables;
    private int current = 0;

    private long transitionDuration;
    private long transitionDelay;

    public RotatingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
        ivOne = new ImageView(context, attrs);
        ivTwo = new ImageView(context, attrs);
        readAttributes(context, attrs);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.RotatingImageView, 0, 0);

        try {
            transitionDuration = a.getInt(R.styleable.RotatingImageView_transitionDuration,
                    DEFAULT_TRANSITION_DURATION);

            transitionDelay = a.getInt(R.styleable.RotatingImageView_transitionDelay,
                    DEFAULT_TRANSITION_DELAY);

            int initialDrawable = a.getResourceId(R.styleable.RotatingImageView_initialSrc, -1);
            initialize(initialDrawable);
        } finally {
            a.recycle();
        }
    }

    private void initialize(int initialDrawable) {
        ivCurrent = ivOne;
        ivNext = ivTwo;

        addView(ivCurrent);
        addView(ivNext);

        Picasso.with(getContext())
                .load(initialDrawable)
                .fit()
                .centerCrop()
                .into(ivCurrent);

        doTransition();
    }

    private void doTransition() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transitionViews();
                doTransition();
            }
        }, transitionDelay);
    }

    private void updateViews() {
        rotateDrawables();
        loadDrawable(getCurrentDrawable());
    }

    private void swapViews() {
        ImageView temp = ivCurrent;
        ivCurrent = ivNext;
        ivNext = temp;
    }

    private void rotateDrawables() {
        current = (current + 1) % drawables.length;
    }

    private int getCurrentDrawable() {
        return drawables[current];
    }

    private void loadDrawable(int resId) {
        Picasso.with(getContext())
                .load(resId)
                .fit()
                .centerCrop()
                .into(ivNext);
    }

    private void transitionViews() {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(ivCurrent, View.ALPHA, 1, 0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(ivNext, View.ALPHA, 0, 1);

        AnimatorSet crossFade = new AnimatorSet();
        crossFade.setDuration(transitionDuration)
                .play(fadeOut)
                .with(fadeIn);

        crossFade.setInterpolator(new AccelerateDecelerateInterpolator());
        crossFade.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                updateViews();
                ivNext.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                swapViews();
                ivNext.setVisibility(INVISIBLE);
            }
        });

        crossFade.start();
    }

    public void setDrawables(int[] drawables) {
        current = 0;
        this.drawables = drawables;
        invalidate();
    }

}
