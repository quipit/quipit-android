package it.quip.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Circle {

    private long uid;
    private String name;
    private List<Quip> quips;

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public List<Quip> getQuips() {
        return quips;
    }

    public static Circle fromJSON(JSONObject circleJson) {
        Circle circle = new Circle();

        try {
            circle.uid = circleJson.getLong("id");
            circle.name = circleJson.getString("name");
            circle.quips = Quip.fromJSONArray(circleJson.getJSONArray("quips"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return circle;
    }

    public static List<Circle> fromJSONArray(JSONArray circlesJson) {
        List<Circle> circles = new ArrayList<>();

        for (int i = 0; i < circlesJson.length(); i++) {

            Circle circle;
            try {
                JSONObject quipJson = circlesJson.getJSONObject(i);
                circle = Circle.fromJSON(quipJson);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            if (circle != null) {
                circles.add(circle);
            }
        }

        return circles;

    }
}
