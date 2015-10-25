package it.quip.android.listener;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public interface ParseModelQueryHandler {
    void onResult(List<ParseObject> scoreList);
    void onException(ParseException e);
}
