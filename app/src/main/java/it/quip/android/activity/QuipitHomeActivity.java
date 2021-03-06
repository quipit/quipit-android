package it.quip.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.adapter.CirclesAdapter;
import it.quip.android.fragment.HomeFeedFragment;
import it.quip.android.fragment.NotificationsFragment;
import it.quip.android.fragment.OnActionRequestedListener;
import it.quip.android.fragment.ViewCircleFragment;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.listener.TagClickListener;
import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.model.User;
import it.quip.android.repository.circle.CirclesResponseHandler;


public class QuipitHomeActivity extends BaseActivity implements TagClickListener {

    private static final int CREATE_CIRCLE_REQUEST = 158;
    private static final int CREATE_QUIP_REQUEST = 321;

    private User mUser;

    private List<Circle> mCircles;
    private CirclesAdapter aCircles;
    private RecyclerView mNavDrawer;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mNotificationBar;
    private ProgressBar mProgressBar;

    private ImageView mNotificationNavigationIcon;
    private HomeFeedFragment mHomeFeedFragment;


    private OnActionRequestedListener mOnActionRequestedListener = new OnActionRequestedListener() {
        @Override
        public void onCreateQuip() {
            createQuip(null);
        }

        @Override
        public void onCreateCircle() {
            createCircle();
        }

        @Override
        public void onCreateQuipInCircle(Circle circle) {
            createQuip(circle);
        }
    };

    private final BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            QuipitApplication.notificationManager().cancelAll();

            if (Notification.NOTIFICATION_RECEIVED_ACTION.equals(action)) {
                Notification notification = (Notification) intent.getExtras().get(Notification.MARSHALL_INTENT_KEY);
                String sender = notification.getSenderUid();
                if (mUser != null && !mUser.getObjectId().equals(sender)) {
                    onNotificationReceive(notification);
                }
            }
        }
    };

    private void registerBroadcastReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver,
                new IntentFilter(Notification.NOTIFICATION_RECEIVED_ACTION));
    }

    private void setupViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.pbProgressAction);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mNotificationNavigationIcon = (ImageView) mToolbar.findViewById(R.id.ivNotificationButton);
        setSupportActionBar(mToolbar);
        mNotificationNavigationIcon.setColorFilter(ContextCompat.getColor(this, R.color.quipit_text));
        mNotificationNavigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotificationFeed();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerContent();
        fetchCircles();

        displayDefaultQuipStream();
        setupNotificationToastBar();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent() {
        mNavDrawer = (RecyclerView) findViewById(R.id.nav_drawer);
        mNavDrawer.setLayoutManager(new LinearLayoutManager(this));

        aCircles.setOnItemClickListener(new CirclesAdapter.OnClickListener() {
            @Override
            public void onClick(Circle circle, int position) {
                selectDrawerItem(position);
            }
        });

        mNavDrawer.setAdapter(aCircles);
        setupHeaderView();
    }

    private void setupHeaderView() {
        View header = findViewById(R.id.nav_header);

        TextView tvName = (TextView) header.findViewById(R.id.tv_name);
        tvName.setText(mUser.getName());

        ImageView ivProfile = (ImageView) header.findViewById(R.id.iv_profile);
        Picasso.with(this)
                .load(mUser.getImageUrl())
                .transform(new CircleTransformation(4, Color.WHITE))
                .fit()
                .centerCrop()
                .into(ivProfile);
    }

    private void fetchCircles() {
        mUser.getCircles(new CirclesResponseHandler() {
            @Override
            public void onSuccess(List<Circle> circles) {
                onCirclesFetched(circles);
            }
        });
    }

    private void onCirclesFetched(List<Circle> circles) {
        mCircles.clear();
        mCircles.addAll(circles);
        aCircles.notifyDataSetChanged();
    }

    private void setupNotificationToastBar() {
        mNotificationBar = (RelativeLayout) findViewById(R.id.toolbar_notification_toast_bard);
    }

    private void selectDrawerItem(int position) {
        Circle circle = mCircles.get(position);
        Fragment circleFragment = ViewCircleFragment.newInstance(circle);

        prepareFragment(circleFragment).commit();
        mDrawerLayout.closeDrawers();
    }

    /**
     * Prepares a fragment to be displayed, returning the fragment transaction. It is up to the
     * callee to decide what to do with it (commit the transaction or commit allowing state loss).
     *
     * In some cases, we'll want to use commitAllowingStateLoss. Specifically, after the state
     * has been saved. See these docs for more info:
     *  http://developer.android.com/reference/android/app/FragmentTransaction.html#commitAllowingStateLoss()
     */
    private FragmentTransaction prepareFragment(Fragment fragment) {
        return prepareFragment(fragment, true);
    }

    private FragmentTransaction prepareFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.fade_out, R.anim.slide_down, R.anim.fade_in);
        ft.replace(R.id.fl_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        return ft;
    }

    private void displayDefaultQuipStream() {
        if (null == mHomeFeedFragment) {
            mHomeFeedFragment = new HomeFeedFragment();
        }

        mHomeFeedFragment.setOnActionRequestedListener(mOnActionRequestedListener);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.hold, R.anim.slide_down);
        ft.replace(R.id.fl_content, mHomeFeedFragment);
        ft.commit();
    }

    private void onCircleCreated() {
        mCircles.clear();
        mCircles.addAll(mUser.getCircles());
        aCircles.notifyDataSetChanged();
        viewCircle(mCircles.get(mCircles.size() - 1));
    }

    private void viewCircle(Circle circle) {
        prepareFragment(ViewCircleFragment.newInstance(circle)).commitAllowingStateLoss();
    }

    private void createCircle() {
        Intent intent = new Intent(this, CreateCircleActivity.class);
        startActivityForResult(intent, CREATE_CIRCLE_REQUEST);
        overridePendingTransition(R.anim.slide_up, R.anim.zoom_out);
    }

    private void createQuip(Circle fromCircle) {
        Intent intent = new Intent(this, CreateQuipActivity.class);
        if (null != fromCircle) {
            intent.putExtra(CreateQuipActivity.COMPOSE_QUIP_CIRCLE_ID, fromCircle.getObjectId());
        }
        startActivityForResult(intent, CREATE_QUIP_REQUEST);
        overridePendingTransition(R.anim.slide_up, R.anim.zoom_out);
    }

    private void showNotificationFeed() {
        mNotificationNavigationIcon.setColorFilter(ContextCompat.getColor(this, R.color.quipit_text));
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        prepareFragment(notificationsFragment).commit();
    }

    @Override
    protected void onDestroy() {
        if(mNotificationReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quipit_home);

        mUser = QuipitApplication.getCurrentUser();
        mCircles = new ArrayList<>();
        aCircles = new CirclesAdapter(this, mCircles);

        registerBroadcastReceivers();
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the current user when coming and going between activities
        mUser = QuipitApplication.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CREATE_CIRCLE_REQUEST == requestCode) {
            if (RESULT_OK == resultCode) {
                onCircleCreated();
            }
        } else if (CREATE_QUIP_REQUEST == requestCode) {
            if (RESULT_OK == resultCode) {
                String createdQuipCircleId = data.getStringExtra(CreateQuipActivity.CREATED_QUIP_CIRCLE_ID);
                if (null != createdQuipCircleId) {
                    Circle c = Circle.findById(mCircles, createdQuipCircleId);
                    viewCircle(c);
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quipit_home, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void clickedTag(CharSequence tag) {
        Toast.makeText(this, tag.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        displayDefaultQuipStream();
    }

    public OnActionRequestedListener getOnActionRequestedListener() {
        return mOnActionRequestedListener;
    }

    public void onNotificationReceive(Notification notification) {
        mNotificationNavigationIcon.setColorFilter(ContextCompat.getColor(this, R.color.quipit_brand));
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
