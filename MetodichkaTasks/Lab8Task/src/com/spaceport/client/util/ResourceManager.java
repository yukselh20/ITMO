package com.spaceport.client.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager {
    private static ResourceBundle bundle;
    private static Locale currentLocale;

    static {
        // Default to English
        setLocale(new Locale("en", "US"));
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("resources.messages", locale);
    }

    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
