package it.quip.android.repository.user;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import it.quip.android.model.Circle;
import it.quip.android.model.User;

public class ParseUserRepository implements UserRepository {
    @Override
    public void getUser(User user) {

    }

    @Override
    public void getFriends(User user, final UsersResponseHandler handler) {
        // TODO: Actually scope this to a user
        ParseQuery.getQuery(User.class).findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    handler.onSuccess(users);
                }
            }
        });
    }

    @Override
    public void getUsersInCircle(Circle circle, UsersResponseHandler handler) {

    }
}
