package it.quip.android.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.quip.android.model.Circle;
import it.quip.android.model.Quip;
import it.quip.android.model.User;

// TODO: remove this class once we have persistence
public class MockUtils {

    private static final Random generator = new Random();
    private static final int YEAR_IN_S = 31540000;

    private static final List<User> fakeUsers = Arrays.asList(
            userWithName("Jonathan Como"),
            userWithName("Dan Buscaglia"),
            userWithName("Hasham Ali"),
            userWithName("Tucker Joseph"),
            userWithName("Brother Darkness"),
            userWithName("Bob Como"),
            userWithName("Robin Como"),
            userWithName("Bob Fannan"),
            userWithName("Vimal Goel")
    );

    private static final List<Circle> fakeCircles = Arrays.asList(
            circleWithName("#thangs"),
            circleWithName("SF Crew")
    );

    private static final List<Quip> fakeQuips = Arrays.asList(
            quipWithText("That is some thangs!"),
            quipWithText("#darksided"),
            quipWithText("You are brother darkness..."),
            quipWithText("#puresided"),
            quipWithText("what is them thangs with some thangs"),
            quipWithText("whats pappin cap'n?"),
            quipWithText("pa-yure"),
            quipWithText("big dog thangs"),
            quipWithText("folsimer")
    );

    public static List<User> getUsers() {
        return fakeUsers;
    }

    public static List<Circle> getCircles() {
        return fakeCircles;
    }

    public static List<Quip> getQuips() {
        return fakeQuips;
    }

    private static User userWithName(String name) {
        User user = new User();

        user.setName(name);
        user.setEmail("");
        user.setImageUrl("https://cloud.githubusercontent.com/assets/1068249/10568603/4ddda6ac-75cf-11e5-8016-8d0c504c0051.png");
        user.setFacebookId(randomId());
        if (name.equals("Hasham Ali")) {
            String s = "Oo7iDf8gIH";
            user.setObjectId(s);
        }
        return user;
    }

    private static Circle circleWithName(String name) {
        List<User> members = new ArrayList<>();
        for (int i = 0; i < generator.nextInt(fakeUsers.size()); i++) {
            members.add(fakeUsers.get(generator.nextInt(fakeUsers.size())));
        }

        Circle circle = new Circle();
        circle.setName(name);
        for (User member : members) {
            circle.addMember(member);
            member.addCircle(circle);
        }

        return circle;
    }

    private static Quip quipWithText(String text) {
        return new Quip(text, randomUser(), randomUser(), randomCircle(), randomTimestamp(), null);
    }

    public static String randomId() {
        return Long.toString(generator.nextInt(Integer.MAX_VALUE));
    }

    private static User randomUser() {
        return fakeUsers.get(generator.nextInt(fakeUsers.size()));
    }

    private static Circle randomCircle() {
        return fakeCircles.get(generator.nextInt(fakeCircles.size()));
    }

    private static long randomTimestamp() {
        return TimeUtils.currentTimestampInS() - generator.nextInt(YEAR_IN_S);
    }

}
