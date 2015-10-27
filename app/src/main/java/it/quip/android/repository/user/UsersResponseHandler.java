package it.quip.android.repository.user;

import java.util.List;

import it.quip.android.model.User;

public interface UsersResponseHandler {
    void onSuccess(List<User> users);
}
