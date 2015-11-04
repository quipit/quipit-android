package it.quip.android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import it.quip.android.R;
import it.quip.android.fragment.FriendSearchListFragment;
import it.quip.android.fragment.SearchFragment;
import it.quip.android.model.BaseParseObject;
import it.quip.android.model.User;

public class SourceQuipActivity extends BaseActivity {

    public static final int SOURCE_QUIP_RESULT = 1112;
    public static final String SELECTED_FRIEND = "SELECTED_FRIEND";

    private Button btPost;
    private FriendSearchListFragment mFriendSearchListFragment;

    private static User sSource;

    private void setupDependencies() {
        mFriendSearchListFragment = FriendSearchListFragment.newInstance();
        mFriendSearchListFragment.setOnSearchListChangedListener(new SearchFragment.OnSearchListChangedListener() {
            @Override
            public void onSelect(BaseParseObject object) {
                finishWithResult();
            }

            @Override
            public void onUnselect(BaseParseObject object) {

            }
        });
    }

    private void setupView() {
        setContentView(R.layout.activity_source_quip);
        btPost = (Button) findViewById(R.id.bt_source_quip_post);
        btPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishWithResult();
            }

        });
    }

    private void showFriendSelectFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_source_quip_friend_picker, mFriendSearchListFragment);
        ft.commit();
    }

    private void finishWithResult() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        sSource = mFriendSearchListFragment.getSelectedValues().get(0);
        finish();
        animateOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDependencies();
        setupView();
        showFriendSelectFragment();
    }

    @Override
    public void onBackPressed() {
        finish();
        animateOut();
    }

    private void animateOut() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static User getSource() {
        User result = sSource;
        sSource = null;
        return result;
    }

}
