package it.quip.android.util;

import android.text.format.DateUtils;

import java.util.Scanner;
import java.util.regex.Pattern;

public class FormatUtils {

    public static final Pattern CIRCLE_PATTERN = Pattern.compile("(@[A-Za-z0-9_-]+)");

    public static final String CIRCLE_MENTION_COLOR = "#FD8917";

    private static final int SECOND_IN_MILLISECONDS = 1000;
    private static final String JUST_NOW = "just now";

    public static String getRelativeTimeAgo(long timeInMillis) {
        return getRelativeTimeAgo(timeInMillis, System.currentTimeMillis());
    }

    private static String getRelativeTimeAgo(long timeInMillis, long relativeTo) {
        if (relativeTo - timeInMillis <= SECOND_IN_MILLISECONDS) {
            return JUST_NOW;
        } else {
            String relativeDateCreated = DateUtils.getRelativeTimeSpanString(
                    timeInMillis, relativeTo, DateUtils.SECOND_IN_MILLIS).toString();
            return shortenDate(relativeDateCreated);
        }
    }

    private static String shortenDate(String relativeDate) {
        Scanner s = dateStringScanner(relativeDate);
        if (s.hasNextInt()) {
            int amount = s.nextInt();
            String unit = s.next();
            return String.format("%d%c", amount, unit.charAt(0));
        } else {
            // Date pattern that was a long time ago
            return relativeDate;
        }
    }

    private static Scanner dateStringScanner(String date) {
        Scanner s = new Scanner(date);
        s.useDelimiter(" ");
        return s;
    }
    
}
