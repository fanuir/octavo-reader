package com.fanuir.octavoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ivy on 7/9/15.
 */
public class Reader extends TextView {

    public Reader(Context context, AttributeSet attrs){
        super(context, attrs);
        setCustomFont(this, context);
    }

    public static void setCustomFont(TextView textview, Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPref.getString("pref_key_story_font", null);
        System.out.println(font);
        setCustomFont(textview, font, context);
    }

    public static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        } else if(font.equals("Sans Serif")){
            textview.setTypeface(Typeface.SANS_SERIF);
        } else if(font.equals("Serif")){
            textview.setTypeface(Typeface.SERIF);
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }
}
