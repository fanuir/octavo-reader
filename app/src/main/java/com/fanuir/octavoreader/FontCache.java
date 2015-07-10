package com.fanuir.octavoreader;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by ivy on 7/9/15.
 */
public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String font, Context context) {
        Typeface tf = fontCache.get(font);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + font);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(font, tf);
        }
        return tf;
    }

}
