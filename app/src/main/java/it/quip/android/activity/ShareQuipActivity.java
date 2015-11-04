package it.quip.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.R;
import it.quip.android.fragment.CirclesSearchGridFragment;
import it.quip.android.model.Circle;

public class ShareQuipActivity extends BaseActivity {

    private Button btPost;
    private CirclesSearchGridFragment mCirclesSearchGridFragment;
    private String mComposeCircleId;

    private static List<Circle> sCircles;

    private void setupDependencies() {
        mCirclesSearchGridFragment = CirclesSearchGridFragment.newInstance(new ArrayList<String>(){{ add(mComposeCircleId); }});
    }

    private void setupView() {
        setContentView(R.layout.activity_share_quip);
        btPost = (Button) findViewById(R.id.bt_share_quip_post);
        btPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishWithResult();
            }

        });
    }

    private void showCircleSelectFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_share_quip_circle_picker, mCirclesSearchGridFragment);
        ft.commit();
    }

    private void finishWithResult() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        sCircles = mCirclesSearchGridFragment.getSelectedValues();
        finish();
        animateOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComposeCircleId = getIntent().getStringExtra(CreateQuipActivity.COMPOSE_QUIP_CIRCLE_ID);
        setupDependencies();
        setupView();
        showCircleSelectFragment();
    }

    @Override
    public void onBackPressed() {
        finish();
        animateOut();
    }

    private void animateOut() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public static List<Circle> getCircles() {
        List<Circle> result = sCircles;
        sCircles = null;
        return result;
    }


}
