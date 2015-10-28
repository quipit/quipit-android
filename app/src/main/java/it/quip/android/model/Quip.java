package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import it.quip.android.QuipitApplication;

@ParseClassName("Quip")
public class Quip extends BaseParseObject implements Parcelable {

    public static final String TEXT = "text";
    public static final String AUTHOR = "author";
    public static final String SOURCE = "source";
    public static final String CIRCLE = "circle";
    public static final String IMAGE_URL = "image_url";

    private String text;
    private User author;
    private User source;
    private Circle circle;
    private String imageUrl;

    public String getText() {
        return getString(TEXT);
    }

    public User getAuthor() {
        return getRelated(AUTHOR);
    }

    public User getSource() {
        return getRelated(SOURCE);
    }

    public Circle getCircle() {
        return getRelated(CIRCLE);
    }
    
    public String getImageUrl() {
        return getString(IMAGE_URL);
    }

    public void setText(String text) {
        this.text = text;
        this.safePut(TEXT, text);
    }

    public void setAuthor(User author) {
        this.author = author;
        this.safePut(AUTHOR, author);
    }

    public void setSource(User source) {
        this.source = source;
        this.safePut(SOURCE, source);
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
        this.safePut(CIRCLE, circle);
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
        dest.writeString(this.imageUrl);
    }

    public Quip() {

    }

    public Quip(Quip clone) {
        this.setText(clone.getText());
        this.setAuthor(clone.getAuthor());
        this.setSource(clone.getSource());
        this.setCircle(clone.getCircle());
        this.setImageUrl(clone.getImageUrl());
    }

    public Quip(String text, User author, User source, Circle circle, long timestamp, String imageUrl) {
        this.setText(text);
        this.setAuthor(author);
        this.setSource(source);
        this.setCircle(circle);
        this.setImageUrl(imageUrl);
    }

    private Quip(Parcel in) {
        this.setText(in.readString());
        this.setAuthor((User) in.readParcelable(User.class.getClassLoader()));
        this.setSource((User) in.readParcelable(User.class.getClassLoader()));
        this.setCircle((Circle) in.readParcelable(Circle.class.getClassLoader()));
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
                        .whereDoesNotExist(CIRCLE)
                        .orderByDescending(CREATED_AT)
                        .find();
            } else {
                quips = getQuery()
                        .whereEqualTo(CIRCLE, circle.getObjectId())
                        .orderByDescending(CREATED_AT)
                        .find();
            }
        } catch (ParseException parseException) {
            Log.e("Parse", "Parse unable to load quips.");
        }

        return quips;
    }

    @Override
    public void saveInternal() {
        User user = QuipitApplication.getCurrentUser();
        Notification notification = new Notification.with(null)
                .sender(user)
                .circle(getCircle())
                .type(Notification.STANDARD_NOTIFICATION)
                .body(user.getName() + " just quipped to circle @" + getCircle().getName())
                .deliver();

        super.saveInternal();
    }
}
