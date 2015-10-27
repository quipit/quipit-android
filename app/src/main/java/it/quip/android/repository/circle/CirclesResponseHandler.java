package it.quip.android.repository.circle;


import java.util.List;

import it.quip.android.model.Circle;

public interface CirclesResponseHandler {
    void onSuccess(List<Circle> circles);
}
