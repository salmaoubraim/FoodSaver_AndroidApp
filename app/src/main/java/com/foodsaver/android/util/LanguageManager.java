package com.foodsaver.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LanguageManager {
    private static final String PREFS_NAME = "FoodSaverApp";
    private static final String KEY_LANGUAGE = "app_language";

    public static void setLanguage(Context context, String languageCode) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
        applyLanguage(context);
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "");
    }

    /** Applique la langue sauvegardée au démarrage de l'app. */
    public static void applyLanguage(Context context) {
        String lang = getLanguage(context);
        if (lang == null || lang.isEmpty()) {
            lang = "fr";
        }
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
        }
        context.getApplicationContext().getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /** Enveloppe le contexte avec la langue choisie (pour attachBaseContext). */
    public static Context wrapContext(Context context) {
        String lang = getLanguage(context);
        if (lang == null || lang.isEmpty()) {
            lang = "fr";
        }
        Locale locale = new Locale(lang);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
        }
        return context.createConfigurationContext(config);
    }
}
