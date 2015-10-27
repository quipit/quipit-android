package it.quip.android.actvitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.SaveCallback;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.CircleHeaderFragment;
import it.quip.android.fragment.InviteFriendsFragment;
import it.quip.android.model.Circle;
import it.quip.android.model.User;

public class CreateCircleActivity extends AppCompatActivity
        implements InviteFriendsFragment.OnFriendsListChangedListener {

    public static final String CREATED_CIRCLE = "it.quip.android.CREATED_CIRCLE";

    private CircleHeaderFragment circleHeaderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);

        if (null == savedInstanceState) {
            setupFragments();
        }
    }

    private void setupFragments() {
        Circle circle = new Circle();
        circle.addMember(QuipitApplication.getCurrentUser());

        circleHeaderFragment = CircleHeaderFragment.newInstance(circle);
        circleHeaderFragment.setEditing(true);

        InviteFriendsFragment inviteFriendsFragment = InviteFriendsFragment.newInstance();
        inviteFriendsFragment.setOnFriendsListChangedListener(this);

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
        Circle createdCircle = circleHeaderFragment.getCircle();
        createdCircle.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                finishWithResult();
            }
        });

        // TODO: do something with theme images
//        Bitmap avatar = circleHeaderFragment.getAvatar();
//        new ParseFile("avatar.jpg", ImageUtils.getBytes(avatar));
//
//        Bitmap background = circleHeaderFragment.getBackground();
//        new ParseFile("background.jpg", ImageUtils.getBytes(background));

    }

    private void finishWithResult() {
        Circle createdCircle = circleHeaderFragment.getCircle();
        Intent data = new Intent();

        // Add circle to all users...
        for (User u : createdCircle.getMembers()) {
            u.addCircle(createdCircle);
            u.saveInBackground();
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onFriendInvited(User friend) {
        circleHeaderFragment.addMember(friend);
    }

    @Override
    public void onFriendUninvited(User friend) {
        circleHeaderFragment.removeMember(friend);
    }
}
