package it.quip.android.model;

import com.parse.ParseException;
import com.parse.ParseObject;


public class BaseParseObject extends ParseObject {

    protected void safePut(String key, Object value) {
        if (value != null) {
            this.put(key, value);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends ParseObject> T getRelated(String key) {
        ParseObject object = getParseObject(key);
        if (null == object) {
            return null;
        }

        try {
            return (T) object.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveInternal() {
        this.saveInBackground();
    }

    @Override
    public boolean equals(Object o) {
        try {
            return ((ParseObject) o).getObjectId().equals(getObjectId());
        } catch (ClassCastException classCastException) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

}
