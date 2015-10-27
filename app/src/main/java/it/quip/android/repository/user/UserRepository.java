package it.quip.android.repository.user;

import it.quip.android.model.Circle;
import it.quip.android.model.User;

public interface UserRepository {
    void getUser(User user);
    void getFriends(User user, UsersResponseHandler handler);
    void getUsersInCircle(Circle circle, UsersResponseHandler handler);
}
