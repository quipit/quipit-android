package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Circle")
public class Circle extends BaseParseObject implements Parcelable {

    public static final String NAME_KEY = "name";
    public static final String MEMBERS_KEY = "members";

    private static final String AVATAR_IMAGE_KEY = "avatar";
    private static final String BACKGROUND_IMAGE_KEY = "background";

    private String name = "";
    private List<User> members = new ArrayList<>();

    private ParseFile avatarImage;
    private ParseFile backgroundImage;

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        this.name = name;
        safePut(NAME_KEY, name);
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        ParseRelation<User> relation = getRelation(MEMBERS_KEY);
        for (User previousMember : this.members) {
            relation.remove(previousMember);
        }

        this.members = members;

        for (User newMember : members) {
            relation.add(newMember);
        }
    }

    public void addMember(User member) {
        getRelation(MEMBERS_KEY).add(member);
        members.add(member);
    }

    public void removeMember(User member) {
        getRelation(MEMBERS_KEY).remove(member);
        members.remove(member);
    }

    public ParseFile getAvatarImage() {
        return avatarImage;
    }

    public String getAvatarImageURL() {
        if (null == avatarImage) {
            return null;
        } else {
            return avatarImage.getUrl();
        }
    }

    public void setAvatarImage(ParseFile avatarImage) {
        this.avatarImage = avatarImage;
        put(AVATAR_IMAGE_KEY, avatarImage);
    }

    public ParseFile getBackgroundImage() {
        return backgroundImage;
    }

    public String getBackgroundImageURL() {
        if (null == backgroundImage) {
            return null;
        } else {
            return backgroundImage.getUrl();
        }
    }

    public void setBackgroundImage(ParseFile backgroundImage) {
        this.backgroundImage = backgroundImage;
        put(BACKGROUND_IMAGE_KEY, backgroundImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public Circle() {

    }

    private Circle(Parcel in) {
        setName(in.readString());
        in.readTypedList(members, User.CREATOR);
    }

    public static final Parcelable.Creator<Circle> CREATOR = new Parcelable.Creator<Circle>() {
        public Circle createFromParcel(Parcel source) {
            return new Circle(source);
        }
        public Circle[] newArray(int size) {
            return new Circle[size];
        }
    };

}
