package com.fanuir.octavoreader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by ivy on 7/22/15.
 */
public class Story2 {

    private JsonObject mMetadata;
    private ArrayList<Chapter> mChapters;

    public Story2(JsonObject metadata){
        this.mMetadata = metadata;
    }

    public Story2(JsonObject metadata, ArrayList<Chapter> chapters){
        this.mMetadata = metadata;
        this.mChapters = chapters;
    }

    public Chapter getChapter(int chapter) {
        // chapter is a number between 1-n, eg chapter 1, chapter 2...
        if (chapter <= mChapters.size() && chapter > 0) {
            return mChapters.get(chapter - 1);
        } else {
            return null;
        }
    }

    public int getCurrentChapter(){
        return mMetadata.get("current_chapter").getAsInt();
    }

    public void setCurrentChapter(int chapter){
        mMetadata.addProperty("current_chapter",chapter);
    }

    public Chapter getNextChapter(){
        setCurrentChapter(getCurrentChapter()+1);
        return getChapter(getCurrentChapter());
    }

    public Chapter getPrevChapter(){
        setCurrentChapter(getCurrentChapter()-1);
        return getChapter(getCurrentChapter());
    }

}
