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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by ivy on 7/9/15.
 */
public class WebReader extends WebView {

    private Story mStory;
    private String headers;

    public WebReader(Context context, AttributeSet attrs){
        super(context, attrs);
        updateHeaders(context);
        System.out.println(headers);
        setFontSize(context);
    }

    public String getFontHtml(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPreferences.getString("pref_key_story_font", "Sans Serif");
        String result;
        if(font.equals("Sans Serif")){
            result = "body { font-family: sans-serif; }";
        } else if(font.equals("Serif")){
            result = "body { font-family: serif; }";
        } else {
            result = "@font-face{ font-family: myFont; src: url('fonts/" + font + "')}"
                    + "body { font-family: myFont; }";
        }
        return result;
    }

    public String updateHeaders(Context context){
        headers = "<head>"
                + "<style>img{display: inline; height: auto; max-width: 100%;}"
                + getFontHtml(context)
                + "</style><link rel='stylesheet' type='text/css' href='reader.css'></head>";
        return headers;
    }

    public void setFontSize(Context context){
        WebSettings settings = getSettings();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int fontSize = Integer.parseInt(sharedPreferences.getString("pref_key_story_font_size", "14"));
        settings.setDefaultFontSize(fontSize);
    }

    public void loadStory(String filename){
        try {
            FileInputStream fis = getContext().openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            mStory = (Story) is.readObject();
            Chapter currChapter = mStory.getCurrentChapter();
            String content = headers + "<body>" + currChapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "UTF-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNextChapter(){
        Chapter chapter = mStory.getNextChapter();
        if(chapter != null) {
            updateHeaders(getContext());
            String content = headers + "<body>" + chapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/",content, "text/html","UTF-8", null);
        } else {
            Toast.makeText(getContext(), "End of story.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPrevChapter(){
        Chapter chapter = mStory.getPrevChapter();
        if(chapter!=null) {
            updateHeaders(getContext());
            String content = headers + "<body>" + chapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/", content, "text/html","UTF-8", null);
        } else {
            Toast.makeText(getContext(), "First chapter.", Toast.LENGTH_SHORT).show();
        }
    }

    public Story getStory(){
        return mStory;
    }
}
