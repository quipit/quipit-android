package it.quip.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import java.util.List;

import it.quip.android.R;
import it.quip.android.fragment.CirclesSearchGridFragment;
import it.quip.android.model.Circle;

public class ShareQuipActivity extends BaseActivity {

    public static final int SHARE_QUIP_RESULT = 1111;
    public static final String SELECTED_CIRCLES = "SELECTED_CIRCLES";

    private Button btPost;
    private CirclesSearchGridFragment mCirclesSearchGridFragment;

    private static List<Circle> sCircles;

    private void setupDependencies() {
        mCirclesSearchGridFragment = CirclesSearchGridFragment.newInstance();
    }

    private void setupView() {
        setContentView(R.layout.activity_share_quip);
        btPost = (Button) findViewById(R.id.bt_share_quip_post);
        btPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sCircles = mCirclesSearchGridFragment.getSelectedValues();
                finish();
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
            }

        });
    }

    private void showCircleSelectFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_share_quip_circle_picker, mCirclesSearchGridFragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDependencies();
        setupView();
        showCircleSelectFragment();
    }

    public static List<Circle> getCircles() {
        List<Circle> result = sCircles;
        sCircles = null;
        return result;
    }


}
