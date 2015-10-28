package it.quip.android.repository.circle;


import android.graphics.Bitmap;

import it.quip.android.model.Circle;
import it.quip.android.model.User;

public interface CircleRepository {
    void getCircle(Circle circle, CircleResponseHandler handler);
    void getAllForUser(User user, CirclesResponseHandler handler);
    void save(Circle circle);
    void saveAndUpload(Circle circle, Bitmap avatar, Bitmap background, CircleResponseHandler handler);
}
