package it.quip.android.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.HomeFeedFragment;
import it.quip.android.fragment.NotificationsFragment;
import it.quip.android.fragment.ViewCircleFragment;
import it.quip.android.listener.TagClickListener;
import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.repository.circle.CirclesResponseHandler;


public class QuipitHomeActivity extends AppCompatActivity implements TagClickListener {

    private static final int CREATE_CIRCLE_REQUEST = 158;
    private static final int CREATE_QUIP_REQUEST = 321;

    private List<Circle> mCircles;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavDrawer;
    private RelativeLayout mNotificationBar;
    private TextView mNotificationBarNotificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quipit_home);

        mCircles = new ArrayList<>();
        registerBroadcastReceivers();
        setupViews();

    }

    private void registerBroadcastReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver,
                new IntentFilter(Notification.NOTIFICATION_RECEIVED_ACTION));
    }

    private final BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Notification.NOTIFICATION_RECEIVED_ACTION.equals(action)) {
                Notification notification = (Notification) intent.getExtras().get(Notification.MARSHALL_INTENT_KEY);
                onNotificationToast(notification);
            }
        }
    };

    @Override
    protected void onDestroy() {
        if(mNotificationReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
        }
        super.onDestroy();
    }


    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavDrawer = (NavigationView) findViewById(R.id.nv_view);
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
        mNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void updateSidebarMenu() {
        Menu circlesSubMenu = mNavDrawer.getMenu().findItem(R.id.navCircles).getSubMenu();
        circlesSubMenu.clear();

        for (int i = 0; i < mCircles.size(); i++) {
            circlesSubMenu.add(0, i, Menu.NONE, mCircles.get(i).getName());
        }
    }

    private void fetchCircles() {
        QuipitApplication.getCurrentUser().getCircles(new CirclesResponseHandler() {
            @Override
            public void onSuccess(List<Circle> circles) {
                onCirclesFetched(circles);
            }
        });
    }

    private void onCirclesFetched(List<Circle> circles) {
        mCircles.clear();
        mCircles.addAll(circles);

        updateSidebarMenu();
    }

    private void setupNotificationToastBar() {
        mNotificationBar = (RelativeLayout) findViewById(R.id.toolbar_notification_toast_bard);
        mNotificationBarNotificationText = (TextView) findViewById(R.id.toolbaNotificationText);
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment;

        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.navNotifications:
                fragment = new NotificationsFragment();
                break;
            default:
                // If we end up here, the menu item selected was one of the user's circles
                Circle circle = mCircles.get(itemId);
                if (null == circle) {
                    throw new RuntimeException("Attempted to select circle id " + itemId
                            + " but it doesn't exist in the menu");
                }

                fragment = ViewCircleFragment.newInstance(circle);
        }

        menuItem.setChecked(true);
        prepareFragment(fragment).commit();
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
        ft.replace(R.id.fl_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        return ft;
    }

    private void displayDefaultQuipStream() {
        // TODO: We shouldn't be creating a new fragment each time. We should manage these
        prepareFragment(new HomeFeedFragment(), false).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quipit_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.miNewCircle:
                createCircle();
                return true;
            case R.id.miNewQuip:
                createQuip();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void createCircle() {
        Intent intent = new Intent(this, CreateCircleActivity.class);
        startActivityForResult(intent, CREATE_CIRCLE_REQUEST);
    }

    private void createQuip() {
        Intent intent = new Intent(this, CreateQuipActivity.class);
        startActivityForResult(intent, CREATE_QUIP_REQUEST);
        overridePendingTransition(R.anim.slide_up, R.anim.zoom_out);
    }

    private void onCircleCreated() {
        mCircles.clear();
        mCircles.addAll(QuipitApplication.getCurrentUser().getCircles());
        updateSidebarMenu();
        viewCircle(mCircles.get(mCircles.size() - 1));
    }

    private void viewCircle(Circle circle) {
        prepareFragment(ViewCircleFragment.newInstance(circle)).commitAllowingStateLoss();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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

    public void onNotificationToast(Notification notification) {
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.notification);
        mNotificationBarNotificationText.setText(notification.getText());
        anim.setTarget(mNotificationBar);
        mNotificationBar.setVisibility(View.VISIBLE);
        mNotificationBar.setAlpha(1);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mNotificationBar.setVisibility(View.GONE);
                mNotificationBar.setAlpha(0);
            }
        });
        anim.start();
    }
}
