package it.quip.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.fragment.CircleHeaderFragment;
import it.quip.android.fragment.FriendsSearchListFragment;
import it.quip.android.fragment.SearchFragment;
import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.model.User;
import it.quip.android.repository.circle.CircleResponseHandler;

public class CreateCircleActivity extends BaseActivity
        implements SearchFragment.OnSearchListChangedListener<User> {

    private CircleHeaderFragment circleHeaderFragment;
    private ProgressDialog pdUploading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);

        setupViews();
        if (null == savedInstanceState) {
            setupFragments();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        animateOut();
    }

    private void setupViews() {
        pdUploading = new ProgressDialog(this, DialogFragment.STYLE_NO_TITLE);
        pdUploading.setCancelable(false);

        Button btnCreate = (Button) findViewById(R.id.btn_create_circle);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCircle();
            }
        });
    }

    private void setupFragments() {
        Circle circle = new Circle();
        circle.addMember(QuipitApplication.getCurrentUser());

        circleHeaderFragment = CircleHeaderFragment.newInstance(circle);
        circleHeaderFragment.setEditing(true);

        FriendsSearchListFragment friendsSearchListFragment = FriendsSearchListFragment.newInstance();
        friendsSearchListFragment.setOnSearchListChangedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_circle_info, circleHeaderFragment);
        ft.replace(R.id.fl_circle_friends, friendsSearchListFragment);
        ft.commit();
    }

    private void createCircle() {
        Circle createdCircle = circleHeaderFragment.getCircle();
        Bitmap avatar = circleHeaderFragment.getAvatar();
        Bitmap background = circleHeaderFragment.getBackground();

        showProgressDialog("Creating the circle " + createdCircle.getName() + "...");
        QuipitApplication.getCircleRepo().saveAndUpload(createdCircle, avatar, background, new CircleResponseHandler() {
            @Override
            public void onSuccess(Circle circle) {
                hideProgressDialog();
                finishWithResult();
            }
        });
    }

    private void finishWithResult() {
        Circle createdCircle = circleHeaderFragment.getCircle();
        Intent data = new Intent();

        // Add circle to all users...
        for (User u : createdCircle.getMembers()) {
            u.addCircle(createdCircle);
            u.saveInBackground();
        }

        new Notification.with(null)
                .body(QuipitApplication.getCurrentUser().getName() + " just added you to circle @" + createdCircle.getName())
                .circle(createdCircle)
                .sender(QuipitApplication.getCurrentUser())
                .type(Notification.STANDARD_NOTIFICATION)
                .imageUrl(createdCircle.getAvatarImageURL())
                .deliver();

        setResult(RESULT_OK, data);
        finish();
        animateOut();
    }

    public void onSelect(User friend) {
        circleHeaderFragment.addMember(friend);
    }

    public void onUnselect(User friend) {
        circleHeaderFragment.removeMember(friend);
    }

    private void showProgressDialog(String message) {
        pdUploading.setMessage(message);
        pdUploading.show();
    }

    private void hideProgressDialog() {
        pdUploading.hide();
    }

    private void animateOut() {
        pdUploading.dismiss();
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_down);
    }
}
