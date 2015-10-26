package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Quip")
public class Quip extends BaseParseObject implements Parcelable {

    private static final String TEXT = "text";
    private static final String AUTHOR_ID = "author_id";
    private static final String SOURCE_ID = "source_id";
    private static final String CIRCLE_ID = "circle_id";
    private static final String TIMESTAMP = "timestamp";
    private static final String IMAGE_URL = "image_url";

    private String text;
    private User author;
    private User source;
    private Circle circle;
    private long timestamp;
    private String imageUrl;

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    public User getSource() {
        return source;
    }

    public Circle getCircle() {
        return circle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setText(String text) {
        this.text = text;
        this.safePut(TEXT, text);
    }

    public void setAuthor(User author) {
        this.author = author;
        this.safePut(AUTHOR_ID, author == null ? null : author.getObjectId());
    }

    public void setSource(User source) {
        this.source = source;
        this.safePut(SOURCE_ID, source == null ? null : source.getObjectId());
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
        this.safePut(CIRCLE_ID, circle == null ? null : circle.getObjectId());
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        this.safePut(TIMESTAMP, timestamp);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.safePut(IMAGE_URL, imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.source, flags);
        dest.writeParcelable(this.circle, flags);
        dest.writeLong(this.timestamp);
        dest.writeString(this.imageUrl);
    }

    public Quip() {

    }

    public Quip(Quip clone) {
        this.setText(clone.getText());
        this.setAuthor(clone.getAuthor());
        this.setSource(clone.getSource());
        this.setCircle(clone.getCircle());
        this.setTimestamp(clone.getTimestamp());
        this.setImageUrl(clone.getImageUrl());
    }

    public Quip(String text, User author, User source, Circle circle, long timestamp, String imageUrl) {
        this.setText(text);
        this.setAuthor(author);
        this.setSource(source);
        this.setCircle(circle);
        this.setTimestamp(timestamp);
        this.setImageUrl(imageUrl);
    }

    private Quip(Parcel in) {
        this.setText(in.readString());
        this.setAuthor((User) in.readParcelable(User.class.getClassLoader()));
        this.setSource((User) in.readParcelable(User.class.getClassLoader()));
        this.setCircle((Circle) in.readParcelable(Circle.class.getClassLoader()));
        this.setTimestamp(in.readLong());
        this.setImageUrl(in.readString());
    }

    public static final Parcelable.Creator<Quip> CREATOR = new Parcelable.Creator<Quip>() {
        public Quip createFromParcel(Parcel source) {
            return new Quip(source);
        }
        public Quip[] newArray(int size) {
            return new Quip[size];
        }
    };

    public static ParseQuery<Quip> getQuery() {
        return ParseQuery.getQuery(Quip.class);
    }

    public static List<Quip> getQuips() {
        return getQuipsByCircle(null);
    }

    public static List<Quip> getQuipsByCircle(Circle circle) {
        List<Quip> quips = null;
        try {
            if (null == circle) {
                // TODO: figure out how to query quips by only your facebook friends...
                quips = getQuery()
                        .whereDoesNotExist(CIRCLE_ID)
                        .orderByDescending(TIMESTAMP)
                        .find();
            } else {
                quips = getQuery()
                        .whereEqualTo(CIRCLE_ID, circle.getObjectId())
                        .orderByDescending(TIMESTAMP)
                        .find();
            }
        } catch (ParseException parseException) {
            Log.e("Parse", "Parse unable to load quips.");
        }

        return quips;
    }
}
