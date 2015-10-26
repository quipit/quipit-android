package it.quip.android.actvitiy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.NotificationsFragment;
import it.quip.android.fragment.QuipFeedFragment;
import it.quip.android.fragment.ViewCircleFragment;
import it.quip.android.listener.TagClickListener;
import it.quip.android.model.Circle;
import it.quip.android.model.User;
import it.quip.android.util.MockUtils;

public class QuipitHomeActivity extends AppCompatActivity implements TagClickListener {

    private static final int CREATE_CIRCLE_REQUEST = 158;
    private static final int CREATE_QUIP_REQUEST = 321;

    private User user;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quipit_home);

        user = QuipitApplication.getCurrentUser();
        for (Circle circle : MockUtils.getCircles()) {
            user.addCircle(circle);
        }

        setupViews();
    }

    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavDrawer = (NavigationView) findViewById(R.id.nv_view);
        setupDrawerContent();
        updateSidebarMenu();

        displayDefaultQuipStream();
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

        for (Circle circle : user.getCircles()) {
            circlesSubMenu.add(0, Integer.parseInt(circle.getObjectId()), Menu.NONE, circle.getName());
        }
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
                Circle circle = user.getCircle(itemId);
                if (null == circle) {
                    throw new RuntimeException("Attempted to select circle id " + itemId
                            + " but it doesnt exist in the menu");
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
        prepareFragment(new QuipFeedFragment(), false).commit();
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
                Circle createdCircle = data.getParcelableExtra(CreateCircleActivity.CREATED_CIRCLE);
                onCircleCreated(createdCircle);
            }
        } else if (CREATE_QUIP_REQUEST == requestCode) {
            // TODO: reload feed...
        }
    }

    private void createCircle() {
        Intent intent = new Intent(this, CreateCircleActivity.class);
        startActivityForResult(intent, CREATE_CIRCLE_REQUEST);
    }

    private void createQuip() {
        Intent intent = new Intent(this, CreateQuipActivity.class);
        startActivityForResult(intent, CREATE_QUIP_REQUEST);
    }

    private void onCircleCreated(Circle createdCircle) {
        user.addCircle(createdCircle);
        updateSidebarMenu();
        prepareFragment(ViewCircleFragment.newInstance(createdCircle)).commitAllowingStateLoss();
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
}
