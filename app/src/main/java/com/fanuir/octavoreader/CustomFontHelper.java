package com.fanuir.octavoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * Created by ivy on 7/9/15.
 */
public class CustomFontHelper {

    public static void setCustomFont(TextView textview, Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPref.getString("pref_key_story_font", null);
        System.out.println(font);
        setCustomFont(textview, font, context);
    }

    public static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        } else if(font.equals("Default")){
            textview.setTypeface(Typeface.SANS_SERIF);
        }
        Typeface tf = FontCache.get("fonts/" + font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }

}

