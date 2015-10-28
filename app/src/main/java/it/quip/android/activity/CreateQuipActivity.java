package it.quip.android.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.FriendSearchListFragment;
import it.quip.android.fragment.QuipComposeFragment;
import it.quip.android.fragment.CirclesSelectListFragment;
import it.quip.android.model.Circle;
import it.quip.android.model.Quip;
import it.quip.android.model.User;
import it.quip.android.util.TimeUtils;

public class CreateQuipActivity
        extends AppCompatActivity
        implements QuipComposeFragment.OnComposeQuipListener {

    private static final String COMPOSE = "compose_fragment";

    private QuipComposeFragment mCreateQuipComposeFragment;
    private FriendSearchListFragment mFriendSearchListFragment;

    private Quip mQuip;
    private Button mBtQuip;
    private FrameLayout mFlCompose;
    private FrameLayout mFlSourcePicker;

    private void setupFragments() {
        mCreateQuipComposeFragment = QuipComposeFragment.newInstance();
        mFriendSearchListFragment = FriendSearchListFragment.newInstance();
    }

    private void setupView() {
        mBtQuip = (Button) findViewById(R.id.bt_quip_create_post);
        mBtQuip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostQuip();
            }
        });

        mFlCompose = (FrameLayout) findViewById(R.id.fl_create_quip_content);
        mFlSourcePicker = (FrameLayout) findViewById(R.id.fl_create_quip_source);
    }

    private void showComposeFragment() {
        mCreateQuipComposeFragment.show(getSupportFragmentManager(), COMPOSE);
    }

    private void showShareFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_create_quip_circle_picker, mCirclesSelectListFragment);
        ft.commit();
    }

    private void onPostQuip() {
        if (mQuip != null) {
            List<Circle> circles = mCirclesSelectListFragment.getSelectedValues();
            long timestamp = TimeUtils.currentTimestampInS();

            // TODO: Figure out how to add a "Share with all friends" option rather than not selecting any circles...
            if (circles.size() > 0) {
                for (Circle circle : circles) {
                    Quip quip = new Quip(mQuip);
                    quip.setCircle(circle);
                    quip.setTimestamp(timestamp);
                    quip.saveInternal();
                }
            } else {
                mQuip.setTimestamp(timestamp);
                mQuip.saveInternal();
            }

            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quip);
        setupFragments();
        setupView();
        showComposeFragment();
    }

    @Override
    public void onComposeQuip(String text, User source) {
        mCreateQuipComposeFragment.dismiss();

        mQuip = new Quip();
        mQuip.setText(text);
        mQuip.setAuthor(QuipitApplication.getCurrentUser());
        mQuip.setSource(source);

        showShareFragment();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
