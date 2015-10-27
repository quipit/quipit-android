package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Circle")
public class Circle extends BaseParseObject implements Parcelable {

    private static final String NAME = "name";
    private static final String MEMBERS = "members";

    private String name = "";
    private List<User> members = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User member) {
        members.add(member);
        this.safePut(MEMBERS, members);
    }

    public void removeMember(User member) {
        members.remove(member);
        this.safePut(MEMBERS, members);
    }

    public void setName(String name) {
        this.name = name;
        this.safePut(NAME, name);
    }

    public void setMembers(List<User> members) {
        this.members = members;
        this.safePut(MEMBERS, members);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedList(members);
    }

    public Circle() {

    }

    private Circle(Parcel in) {
        this.name = in.readString();
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
