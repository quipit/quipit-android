package it.quip.android.util;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.quip.android.R;

public class FormatUtil {

    public static final Pattern CIRCLE_PATTERN = Pattern.compile("(@[A-Za-z0-9_-]+)");

    public static final String CIRCLE_MENTION_COLOR = "#FD8917";
}
