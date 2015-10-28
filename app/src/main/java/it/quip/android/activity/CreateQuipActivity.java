package it.quip.android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.FriendSearchListFragment;
import it.quip.android.fragment.QuipComposeFragment;
import it.quip.android.fragment.SearchListFragment;
import it.quip.android.model.Circle;
import it.quip.android.model.Quip;
import it.quip.android.model.User;

public class CreateQuipActivity
        extends AppCompatActivity
        implements QuipComposeFragment.OnSearchFriend, SearchListFragment.OnSearchListChangedListener<User> {

    public static final String CREATED_QUIP_CIRCLE_ID = "CREATED_QUIP_CIRCLE_ID";
    private static final int SHARE_QUIP_REQUEST = 479;

    private QuipComposeFragment mCreateQuipComposeFragment;
    private FriendSearchListFragment mFriendSearchListFragment;

    private Button mBtShare;

    private void setupFragments() {
        mCreateQuipComposeFragment = QuipComposeFragment.newInstance();
        mFriendSearchListFragment = FriendSearchListFragment.newInstance();
        mFriendSearchListFragment.setUseCustomInput(true);
        mFriendSearchListFragment.setOnSearchListChangedListener(this);
    }

    private void setupView() {
        mBtShare = (Button) findViewById(R.id.bt_quip_create_share);
        mBtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareFragment();
            }
        });
    }

    private void showComposeFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_create_quip_content, mCreateQuipComposeFragment);
        ft.replace(R.id.fl_create_quip_source, mFriendSearchListFragment);
        ft.commit();
    }

    private void showShareFragment() {
        Intent i = new Intent(CreateQuipActivity.this, ShareQuipActivity.class);
        startActivityForResult(i, SHARE_QUIP_REQUEST);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHARE_QUIP_REQUEST) {
            List<Circle> selectedCircles = ShareQuipActivity.getCircles();
            if (null != selectedCircles) {
                createQuip(selectedCircles);
            }

            Intent i = new Intent();
            if (selectedCircles.size() > 0) {
                i.putExtra(CREATED_QUIP_CIRCLE_ID, selectedCircles.get(0).getObjectId());
            }

            setResult(RESULT_OK, i);
            finish();

            overridePendingTransition(R.anim.slide_down, R.anim.zoom_in);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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

    public void createQuip(List<Circle> circles) {
        Quip quip = new Quip();
        quip.setText(mCreateQuipComposeFragment.getBody());
        quip.setAuthor(QuipitApplication.getCurrentUser());

        if (mFriendSearchListFragment.getSelectedValues().size() > 0) {
            quip.setSource(mFriendSearchListFragment.getSelectedValues().get(0));
        }

        if (circles.size() > 0) {
            for (Circle circle : circles) {
                Quip newQuip = new Quip(quip);
                newQuip.setCircle(circle);
                newQuip.saveInternal();
            }
        } else {
            quip.saveInternal();
        }
    }

    @Override
    public void onSearchFriend(String text) {
        mFriendSearchListFragment.search(text);
    }

    @Override
    public void onSelect(User object) {
        mCreateQuipComposeFragment.setSourceName(object.getName());
    }

    @Override
    public void onUnselect(User object) {
        mCreateQuipComposeFragment.setSourceName("anon");
    }
}
