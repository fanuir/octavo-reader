package com.fanuir.octavoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
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
        setMovementMethod(LinkMovementMethod.getInstance());
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
            String fileName = "storyObject" + id + ".ser";
            FileInputStream fis = getContext().openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);

            mStory = (Story) is.readObject();
            Chapter currChapter = mStory.getCurrentChapter();
            this.setText(Html.fromHtml(currChapter.getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNextChapter(){
        Chapter chapter = mStory.getNextChapter();
        if(chapter != null) {
            this.setText(Html.fromHtml(chapter.getContent()));
        } else {
            Toast.makeText(getContext(), "End of story.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPrevChapter(){
        Chapter chapter = mStory.getPrevChapter();
        if(chapter!=null) {
            this.setText(Html.fromHtml(chapter.getContent()));
        } else {
            Toast.makeText(getContext(), "First chapter.", Toast.LENGTH_SHORT).show();
        }
    }

    public Story getStory(){
        return mStory;
    }
}
