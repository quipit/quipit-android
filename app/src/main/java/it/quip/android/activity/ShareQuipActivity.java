package it.quip.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.R;
import it.quip.android.fragment.CirclesSelectListFragment;
import it.quip.android.model.Circle;

public class ShareQuipActivity extends AppCompatActivity {

    public static final int SHARE_QUIP_RESULT = 1111;
    public static final String SELECTED_CIRCLES = "SELECTED_CIRCLES";

    private Button btPost;
    private CirclesSelectListFragment mCirclesSelectListFragment;

    private List<Circle> mCircles;

    private void setupDependencies() {
        mCirclesSelectListFragment = CirclesSelectListFragment.newInstance();
    }

    private void setupView() {
        setContentView(R.layout.activity_share_quip);
        btPost = (Button) findViewById(R.id.bt_share_quip_post);
        btPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCircles = mCirclesSelectListFragment.getSelectedValues();

                Intent responseData = new Intent();
                responseData.putParcelableArrayListExtra(SELECTED_CIRCLES, (ArrayList<Circle>) mCircles);
                setResult(RESULT_OK, responseData);
                finish();
            }

        });
    }

    private void showCircleSelectFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_share_quip_circle_picker, mCirclesSelectListFragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDependencies();
        setupView();
        showCircleSelectFragment();
    }


}
