package com.fanuir.octavoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ivy on 7/9/15.
 */
public class WebReader extends WebView {

    private Story mStory;
    private String mHeaders;

    public WebReader(Context context){
        super(context);
        updateHeaders(context);
        setHorizontalScrollBarEnabled(false);
        setFontSize(context);
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (view.getProgress() == 100) {
                    System.out.println("Progress 100%");
                    loadLastPosition();
                }
            }
        });
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public String getFontHtml(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPreferences.getString("pref_key_story_font", "Original");
        String result;
        if(font.equals("Original")){
            result = "";
        } else if(font.equals("Sans Serif")){
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
        mHeaders = "<head>"
                + "<style>img{display: inline; height: auto; max-width: 100%;}"
                + getFontHtml(context)
                + "</style><link rel='stylesheet' type='text/css' href='reader.css'>"
                + "<script src='reader.js'></script>"
                + "</head>";
        return mHeaders;
    }

    public boolean isFirstChapter(){
        return mStory.getCurrentChapterNum() == 1;
    }

    public boolean isLastChapter(){
        return mStory.getCurrentChapterNum() == mStory.getChapters().size();
    }

    public String getHeaders(){
        return mHeaders;
    }

    public void setFontSize(Context context){
        WebSettings settings = getSettings();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int fontSize = Integer.parseInt(sharedPreferences.getString("pref_key_story_font_size", "14"));
        //System.out.println(fontSize);
        settings.setDefaultFontSize(fontSize);
    }

    public void loadStory(JsonObject metadata){
        try {
            String filename = metadata.get("id").getAsString();
            FileInputStream fis = getContext().openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            ArrayList<Chapter> chapters = (ArrayList<Chapter>) is.readObject();
            mStory = new Story(metadata, chapters);
            mStory.setLastOpened(new Date().getTime());
            loadLastChapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadChapter(int index){
        Chapter chapter = mStory.getChapter(index+1);
        if(chapter != null) {
            updateHeaders(getContext());
            String content = mHeaders + "<body>" + chapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "UTF-8", null);
            mStory.setLastPosition(0);
        }
    }

    public void loadLastChapter(){
        Chapter chapter = mStory.getCurrentChapter();
        if(chapter != null) {
            updateHeaders(getContext());
            String content = mHeaders + "<body>" + chapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "UTF-8", null);
        }
    }

    public void loadNextChapter(){
        Chapter chapter = mStory.getNextChapter();
        if(chapter != null) {
            updateHeaders(getContext());
            String content = mHeaders + "<body>" + chapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/",content, "text/html","UTF-8", null);
            mStory.setLastPosition(0);
        } else {
            Toast.makeText(getContext(), "End of story.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPrevChapter(){
        Chapter chapter = mStory.getPrevChapter();
        if(chapter!=null) {
            updateHeaders(getContext());
            String content = mHeaders + "<body>" + chapter.getContent() + "</body>";
            this.loadDataWithBaseURL("file:///android_asset/", content, "text/html","UTF-8", null);
            mStory.setLastPosition(0);
        } else {
            Toast.makeText(getContext(), "First chapter.", Toast.LENGTH_SHORT).show();
        }
    }

    public Story getStory(){
        return mStory;
    }

    public void saveStoryState(){
        float position = calculateProgress();
        mStory.setLastPosition(position);
        System.out.println("Last position %: " + mStory.getLastPosition());
        ArchiveStoryUtils.saveMetadataToFile(getContext(), mStory.getMetadata(), false);
    }

    public void loadStoryState(){
        mStory.setMetadata(ArchiveStoryUtils.loadStoryMetadataFromFile(getContext(), mStory.getId()));
        System.out.println("Loaded current chapter: " + mStory.getCurrentChapter());
        loadLastChapter();
    }

    public void loadLastPosition(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                float size = getContentHeight() - getTop();
                float positionF = size * mStory.getLastPosition();
                int position = Math.round(getTop() + positionF);
                scrollTo(0, position);
                System.out.println("Scrolled to last position.");
            }
        }, 500);
    }

    public float calculateProgress() {
        float positionTopView = getTop();
        float contentHeight = getContentHeight();
        float currentScrollPosition = getScrollY();
        float percent = (currentScrollPosition - positionTopView) / contentHeight;
        return percent;
    }
}
