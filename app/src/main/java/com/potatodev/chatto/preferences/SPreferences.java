package com.potatodev.chatto.preferences;

import android.content.Context;

public class SPreferences {
    private static final String PREFERENCE_FILENAME = "authentication";
    private static final int PREFERENCE_MODE = Context.MODE_PRIVATE;
    private static final String KEY_IS_AUTH = "AUTH";
    private static final String KEY_PASS = "PASS";

    public static String getPreferenceFilename() {
        return PREFERENCE_FILENAME;
    }

    public static int getPreferenceMode() {
        return PREFERENCE_MODE;
    }

    public static String getKeyIsAuth() {
        return KEY_IS_AUTH;
    }

    public static String getKeyPass() {
        return KEY_PASS;
    }
}
