package it.quip.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Quip {

    private long uid;
    private String text;
    private long sourceId;
    private String sourceName;
    private Circle circle;

    public long getUid() {
        return uid;
    }

    public Circle getCircle() {
        return circle;
    }

    public String getText() {
        return text;
    }

    public long getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public static Quip fromJSON(JSONObject quipJson) {
        Quip quip = new Quip();

        try {
            quip.uid = quipJson.getLong("id");
            quip.circle = Circle.fromJSON(quipJson.getJSONObject("circle"));
            quip.text = quipJson.getString("text");
            quip.sourceId = quipJson.getLong("source_id");
            quip.sourceName = quipJson.getString("source_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return quip;
    }

    public static List<Quip> fromJSONArray(JSONArray quipsJson) {
        List<Quip> quips = new ArrayList<>();

        for (int i = 0; i < quipsJson.length(); i++) {

            Quip quip;
            try {
                JSONObject quipJson = quipsJson.getJSONObject(i);
                quip = Quip.fromJSON(quipJson);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            if (quip != null) {
                quips.add(quip);
            }
        }

        return quips;
    }

}
