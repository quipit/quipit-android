package it.quip.android.actvitiy;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import it.quip.android.R;
import it.quip.android.fragment.NotificationsFragment;
import it.quip.android.fragment.QuipStreamFragment;

public class QuipitHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quipit_home);

        setupViews();
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        dlDrawer.setDrawerListener(drawerToggle);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv_view);
        setupDrawerContent(nvDrawer);

        displayDefaultQuipStream();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(
                this, dlDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment;

        switch (menuItem.getItemId()) {
            case R.id.nav_notifications:
                fragment = new NotificationsFragment();
                break;
            case R.id.nav_circles:
                Log.w("DEBUG", "Circles not implemented yet");
            default:
                // TODO: Once we have all of our nav items wired up, this should be unreachable
                throw new RuntimeException("The id " + menuItem.getItemId() +
                        " isn't recognized as a valid nav menu item");
        }

        menuItem.setChecked(true);
        displayFragment(menuItem.getTitle(), fragment);
        dlDrawer.closeDrawers();
    }

    private void displayFragment(CharSequence title, Fragment fragment) {
        displayFragment(title, fragment, true);
    }

    private void displayFragment(CharSequence title, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        ft.commit();
    }

    private void displayDefaultQuipStream() {
        // TODO: We shouldn't be creating a new fragment each time. We should manage these
        displayFragment("Home", new QuipStreamFragment(), false);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
