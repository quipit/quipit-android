package it.quip.android.service;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesService {

    private SharedPreferences _preferences;
    private SharedPreferences.Editor _preferencesEditor;

    public PreferencesService(Context sourceActivity, String preferenceFile) {
        _preferences = sourceActivity.getSharedPreferences(preferenceFile, Context.MODE_PRIVATE);
        _preferencesEditor = _preferences.edit();
    }

    public void setPreference(String key, String value) {
        _preferencesEditor.putString(key, value);
        _preferencesEditor.commit();
    }

    public String getPreference(String key, String defaultValue) {
        return _preferences.getString(key, defaultValue);
    }

}