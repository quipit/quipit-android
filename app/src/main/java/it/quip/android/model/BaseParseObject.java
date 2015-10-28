package it.quip.android.model;

import com.parse.ParseObject;


public class BaseParseObject extends ParseObject {

    protected void safePut(String key, Object value) {
        if (value != null) {
            this.put(key, value);
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
