package com.fanuir.octavoreader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ivy on 7/22/15.
 */
public class Story {

    private JsonObject mMetadata;
    private ArrayList<Chapter> mChapters;

    public Story(JsonObject metadata, ArrayList<Chapter> chapters){
        this.mMetadata = metadata;
        this.mChapters = chapters;
    }

    public JsonObject getMetadata(){
        return mMetadata;
    }

    public void setMetadata(JsonObject metadata){
        mMetadata = metadata;
    }

    public String getId(){
        return mMetadata.get("id").getAsString();
    }

    public String getTitle(){
        return mMetadata.get("title").getAsString();
    }

    public JsonArray getAuthors(){
        return mMetadata.get("authors").getAsJsonArray();
    }

    public JsonArray getFandoms(){
        return mMetadata.get("fandoms").getAsJsonArray();
    }

    public Chapter getChapter(int chapter) {
        // chapter is a number between 1-n, eg chapter 1, chapter 2...
        if (chapter <= mChapters.size() && chapter > 0) {
            setCurrentChapterNum(chapter);
            return mChapters.get(chapter - 1);
        } else {
            return null;
        }
    }

    public int getCurrentChapterNum(){
        return mMetadata.get("current_chapter").getAsInt();
    }

    public Chapter getCurrentChapter(){
        return getChapter(getCurrentChapterNum());
    }

    public float getLastPosition(){
        return mMetadata.get("last_position").getAsFloat();
    }

    public void setLastPosition(float position){
        mMetadata.addProperty("last_position", position);
    }

    public void setLastOpened(long lastOpened){
        mMetadata.addProperty("last_opened", lastOpened);
    }

    public void setCurrentChapterNum(int chapter){
        mMetadata.addProperty("current_chapter", chapter);
        System.out.println("Current chapter is: " + chapter);
    }

    public Chapter getNextChapter(){
        return getChapter(getCurrentChapterNum()+1);
    }

    public Chapter getPrevChapter(){
        return getChapter(getCurrentChapterNum()-1);
    }

    public String toString(){
        String authors = LibraryUtils.printJsonArray(getAuthors());
        String fandoms = LibraryUtils.printJsonArray(getFandoms());

        int wordCount = mMetadata.get("word_count").getAsInt();
        String story = String.format("%s by %s: %d words\n%s", getTitle(), authors, wordCount, fandoms);
        return story;
    }

    public ArrayList<Chapter> getChapters() {
        return mChapters;
    }
}
