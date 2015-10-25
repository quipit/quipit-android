package it.quip.android.actvitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.CircleHeaderFragment;
import it.quip.android.fragment.InviteFriendsFragment;
import it.quip.android.model.Circle;
import it.quip.android.model.User;
import it.quip.android.util.MockUtils;

public class CreateCircleActivity extends AppCompatActivity {

    public static final String CREATED_CIRCLE = "it.quip.android.CREATED_CIRCLE";

    private CircleHeaderFragment circleHeaderFragment;
    private InviteFriendsFragment inviteFriendsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);

        if (null == savedInstanceState) {
            setupFragments();
        }
    }

    private void setupFragments() {
        circleHeaderFragment = CircleHeaderFragment.newInstance(new Circle());
        inviteFriendsFragment = InviteFriendsFragment.newInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_circle_info, circleHeaderFragment);
        ft.replace(R.id.fl_circle_friends, inviteFriendsFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miCreateCircle:
                createCircle();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createCircle() {
        Circle circle = circleHeaderFragment.getCircle();
        List<User> invitedFriends = inviteFriendsFragment.getInvitedFriends();
        Circle createdCircle = MockUtils.circleWithNameAndMembers(circle.getName(), invitedFriends);
        createdCircle.addMember(QuipitApplication.getCurrentUser());

        Intent data = new Intent();
        data.putExtra(CREATED_CIRCLE, createdCircle);

        setResult(RESULT_OK, data);
        finish();
    }
}
