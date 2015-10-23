package it.quip.android.view;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.graphics.Color;
import android.text.Spannable;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.quip.android.listener.CircleTagClicked;

public class CircleTagTextView {

    private String mCircleColor;
    private CircleTagClicked mTagClick;
    private boolean mHypeLinkEnabled;

    public SpannableStringBuilder circleParse(String nTagString,
                                                   CircleTagClicked TagClick,
                                                   boolean useLink, String circleColor) {

        mCircleColor = circleColor;
        mHypeLinkEnabled = useLink;
        mTagClick = TagClick;
        Pattern circlePattern = Pattern.compile("(@[a-zA-Z0-9_-]+)");

        SpannableStringBuilder string = new SpannableStringBuilder(nTagString);
        CharSequence spanText;
        int start;
        int end;
        Matcher m = circlePattern.matcher(nTagString);
        while (m.find()) {
            start = m.start();
            end = m.end();
            spanText = nTagString.subSequence(start, end);
            final CharSequence mLastTextSpan = spanText;
            string.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Log.d("Circle Clicked", String.format("Clicked", mLastTextSpan));
                    mTagClick.clickedTag(mLastTextSpan);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(Color.parseColor(mCircleColor));
                    ds.setUnderlineText(mHypeLinkEnabled);
                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return string;
    }

}
