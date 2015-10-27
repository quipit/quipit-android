package it.quip.android.repository.circle;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.model.Circle;
import it.quip.android.model.User;
import it.quip.android.repository.user.UsersResponseHandler;

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

                        handler.onSuccess(circles);
                    }
                });
    }

    @Override
    public void save(Circle circle) {

    }

}
