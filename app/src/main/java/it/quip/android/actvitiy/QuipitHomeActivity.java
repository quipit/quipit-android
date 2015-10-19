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

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.NotificationsFragment;
import it.quip.android.fragment.QuipStreamFragment;
import it.quip.android.fragment.ViewCircleFragment;
import it.quip.android.model.Circle;
import it.quip.android.model.User;
import it.quip.android.util.MockUtils;

public class QuipitHomeActivity extends AppCompatActivity {

    private static final int CREATE_CIRCLE_REQUEST = 158;

    private User user;

    private Toolbar mToolbar;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView nvDrawer;

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

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        dlDrawer.setDrawerListener(mDrawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent();
        updateSidebarMenu();

        displayDefaultQuipStream();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(
                this, dlDrawer, mToolbar, R.string.drawerOpen, R.string.drawerClose);
    }

    private void setupDrawerContent() {
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void updateSidebarMenu() {
        Menu circlesSubMenu = nvDrawer.getMenu().findItem(R.id.navCircles).getSubMenu();
        circlesSubMenu.clear();

        for (Circle circle : user.getCircles()) {
            circlesSubMenu.add(0, (int) circle.getUid(), Menu.NONE, circle.getName());
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
        dlDrawer.closeDrawers();
    }

    /**
     * Prepares a fragment to be displayed, returning the fragment transaction. It is up to the
     * callee to decide what to do with it (commit the transaction or commit allowing state loss).
     */
    private FragmentTransaction prepareFragment(Fragment fragment) {
        return prepareFragment(fragment, true);
    }

    private FragmentTransaction prepareFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        return ft;
    }

    private void displayDefaultQuipStream() {
        // TODO: We shouldn't be creating a new fragment each time. We should manage these
        prepareFragment(new QuipStreamFragment(), false).commit();
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
                dlDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.miNewCircle:
                createCircle();
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
        }
    }

    private void createCircle() {
        Intent intent = new Intent(this, CreateCircleActivity.class);
        startActivityForResult(intent, CREATE_CIRCLE_REQUEST);
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
}
