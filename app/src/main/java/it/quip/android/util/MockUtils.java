package it.quip.android.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.quip.android.model.Circle;
import it.quip.android.model.User;

// TODO: remove this class once we have persistence
public class MockUtils {

    private static final Random generator = new Random();

    private static final List<User> fakeUsers = Arrays.asList(
            userWithName("Jonathan Como"),
            userWithName("Dan Buscaglia"),
            userWithName("Hasham Ali"),
            userWithName("Tucker Joseph")
    );

    private static final List<Circle> fakeCircles = Arrays.asList(
            circleWithNameAndMembers("#thangs", fakeUsers),
            circleWithNameAndMembers("SF Crew", fakeUsers)
    );

    public static List<User> getUsers() {
        return fakeUsers;
    }

    public static List<Circle> getCircles() {
        return fakeCircles;
    }

    public static User userWithName(String name) {
        long userId = randomId();
        String userAsJson = String.format(
                "{\"email\": \"\", \"name\": \"%s\", \"id\": %d}", name, userId);

        return User.fromJSON(jsonFromString(userAsJson));
    }

    public static Circle circleWithNameAndMembers(String name, List<User> members) {
        long circleId = randomId();
        String circleAsJson = String.format(
                "{\"name\": \"%s\", \"members\": [], \"id\": %d}", name, circleId);

        Circle circle = Circle.fromJSON(jsonFromString(circleAsJson));
        if (circle != null) {
            for (User member : members) {
                circle.addMember(member);
            }
        }

        return circle;
    }

    private static JSONObject jsonFromString(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("You dun fucked up");
        }
    }

    private static long randomId() {
        return generator.nextInt(Integer.MAX_VALUE);
    }

}
