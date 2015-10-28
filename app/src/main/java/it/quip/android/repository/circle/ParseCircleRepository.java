package it.quip.android.repository.circle;


import android.graphics.Bitmap;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.model.Circle;
import it.quip.android.model.User;
import it.quip.android.repository.user.UsersResponseHandler;
import it.quip.android.util.ImageUtils;

public class ParseCircleRepository implements CircleRepository {

    @Override
    public void getCircle(Circle circle, final CircleResponseHandler handler) {
        ParseObject.createWithoutData(Circle.class, circle.getObjectId())
                .fetchInBackground(new GetCallback<Circle>() {
                    @Override
                    public void done(final Circle fetchedCircle, ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        }

                        // Will need to get the members here as well
                        if (fetchedCircle != null) {
                            getMembers(fetchedCircle, new UsersResponseHandler() {
                                @Override
                                public void onSuccess(List<User> users) {
                                    fetchedCircle.setMembers(users);
                                    handler.onSuccess(fetchedCircle);
                                }
                            });
                        }
                    }
                });
    }

    private void getMembers(Circle circle, final UsersResponseHandler handler) {
        circle.getRelation(Circle.MEMBERS_KEY).getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> fetchedUsers, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }

                List<User> users = new ArrayList<>();
                for (ParseObject user : fetchedUsers) {
                    users.add((User) user);
                }

                handler.onSuccess(users);
            }
        });
    }

    @Override
    public void getAllForUser(User user, final CirclesResponseHandler handler) {
        ParseQuery.getQuery(Circle.class)
                .whereEqualTo(Circle.MEMBERS_KEY, user)
                .findInBackground(new FindCallback<Circle>() {
                    @Override
                    public void done(List<Circle> circles, ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                            circles = new ArrayList<>();
                        }

                        if (handler != null) {
                            handler.onSuccess(circles);
                        }
                    }
                });
    }

    @Override
    public void save(Circle circle) {

    }

    @Override
    public void saveAndUpload(final Circle circle, Bitmap avatar, Bitmap background, final CircleResponseHandler handler) {
        ParseFile avatarFile = new ParseFile("avatar.jpg", ImageUtils.getBytes(avatar));
        ParseFile backgroundFile = new ParseFile("background.jpg", ImageUtils.getBytes(background));

        circle.setAvatarImage(avatarFile);
        circle.setBackgroundImage(backgroundFile);

        try {
            avatarFile.save();
            backgroundFile.save();
        } catch (ParseException e) {
            // TODO: put an error handler here
            e.printStackTrace();
        }

        circle.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }

                handler.onSuccess(circle);
            }
        });
    }
}
