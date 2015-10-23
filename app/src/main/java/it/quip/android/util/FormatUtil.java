package it.quip.android.util;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.quip.android.R;

public class FormatUtil {

    public static final Pattern circlePattern = Pattern.compile("(@[A-Za-z0-9_-]+)");

    public static final String CIRCLE_MENTION_COLOR = "#FD8917";

    public static Spannable formatQuipitText(String unformattedText) {
        StringBuffer sb = new StringBuffer(unformattedText.length());
        Matcher o = circlePattern.matcher(unformattedText);

        while (o.find()) {
            o.appendReplacement(sb,
                    "<font style=\"bold\" color=\""
                    + CIRCLE_MENTION_COLOR
                    + "\">" + o.group(1)
                    + "</font>");

        }
        o.appendTail(sb);
        return(Spannable) Html.fromHtml(sb.toString());
    }
}
