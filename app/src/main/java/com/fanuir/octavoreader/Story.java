package com.fanuir.octavoreader;

import java.util.ArrayList;

/**
 * Created by ivy on 7/10/15.
 */
public class Story {

    private ArrayList<Author> mAuthors;
    private String mTitle;
    private ArrayList<Chapter> mChapters;
    private String mStoryId;
    private int mCurrentChapter;

    public Story(){
        mAuthors = new ArrayList<Author>();
        mTitle = "Untitled";
        mChapters = new ArrayList<Chapter>();
        mStoryId = "000000";
        mCurrentChapter = 1;
    }

    public ArrayList<Author> getAuthors() {
        return mAuthors;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getSize(){
        return mChapters.size();
    }

    public String getStoryId() {
        return mStoryId;
    }

    public ArrayList<Chapter> getChapters() {
        return mChapters;
    }

    public Chapter getChapter(int chapter){
        // chapter is a number between 1-n, eg chapter 1, chapter 2...
        return mChapters.get(chapter - 1);
    }

    public Chapter getNextChapter(){
        return getChapter(mCurrentChapter++ + 1);
    }
}
