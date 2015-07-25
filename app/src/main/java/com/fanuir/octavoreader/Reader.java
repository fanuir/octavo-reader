package com.fanuir.octavoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by ivy on 7/9/15.
 */
public class Reader extends TextView {

    private static Story mStory;

    public Reader(Context context, AttributeSet attrs){
        super(context, attrs);
        setCustomFont(context);
        setFontSize(context);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setFontSize(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int fontSize = Integer.parseInt(sharedPreferences.getString("pref_key_story_font_size", "14"));
        setTextSize(TypedValue.COMPLEX_UNIT_SP,fontSize);
    }

    public void setCustomFont(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPref.getString("pref_key_story_font", null);
        if(font == null) {
            return;
        } else if(font.equals("Sans Serif")){
            this.setTypeface(Typeface.SANS_SERIF);
        } else if(font.equals("Serif")){
            this.setTypeface(Typeface.SERIF);
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null) {
            this.setTypeface(tf);
        }
    }

    public void loadStory(String id){
        try {
            String fileName = "ao3-" + id;
            FileInputStream fis = getContext().openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);

            mStory = (Story) is.readObject();
            Chapter currChapter = mStory.getCurrentChapter();
            Spanned content = Html.fromHtml(currChapter.getContent());
            this.setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNextChapter(){
        Chapter chapter = mStory.getNextChapter();
        if(chapter != null) {
            Spanned content = Html.fromHtml(chapter.getContent());
            this.setText(content);
        } else {
            Toast.makeText(getContext(), "End of story.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPrevChapter(){
        Chapter chapter = mStory.getPrevChapter();
        if(chapter!=null) {
            Spanned content = Html.fromHtml(chapter.getContent());
            this.setText(content);
        } else {
            Toast.makeText(getContext(), "First chapter.", Toast.LENGTH_SHORT).show();
        }
    }

    public Story getStory(){
        return mStory;
    }
}
