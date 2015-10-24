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

import it.quip.android.listener.TagClickListener;

public class TagTextSpanner {

    private String mCircleColor;
    private boolean mHypeLinkEnabled;
    private TagClickListener mTagClickListener;

    public SpannableStringBuilder tagParse(Pattern circlePattern,
                                           String tagString,
                                           TagClickListener listener,
                                           boolean useLink,
                                           String circleColor) {

        mCircleColor = circleColor;
        mHypeLinkEnabled = useLink;
        mTagClickListener = listener;
        SpannableStringBuilder string = new SpannableStringBuilder(tagString);
        CharSequence spanText;
        int start;
        int end;
        Matcher m = circlePattern.matcher(tagString);
        while (m.find()) {
            start = m.start();
            end = m.end();
            spanText = tagString.subSequence(start, end);
            final CharSequence mLastTextSpan = spanText;
            string.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Log.d("Circle Clicked", String.format("Clicked", mLastTextSpan));
                    mTagClickListener.clickedTag(mLastTextSpan);
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
