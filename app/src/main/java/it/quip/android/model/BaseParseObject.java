package it.quip.android.model;

import com.parse.ParseObject;

import it.quip.android.util.MockUtils;


public class BaseParseObject extends ParseObject {

    protected void safePut(String key, Object value) {
        if (value != null) {
            this.put(key, value);
        }
    }

    @Override
    public String getObjectId() {
        String objectId = super.getObjectId();
        if (null == objectId) {
            objectId = MockUtils.randomId();
        }

        return objectId;
    }

    public void saveInternal() {
        this.saveInBackground();
    }
}
