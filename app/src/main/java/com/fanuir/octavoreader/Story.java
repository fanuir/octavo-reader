package com.fanuir.octavoreader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ivy on 7/10/15.
 */
public class Story implements Serializable {

    private String mTitle;
    private ArrayList<String> mAuthors;
    private ArrayList<Chapter> mChapters;
    private ArrayList<String> mFandoms;
    private String mStoryId;
    private String mSummary;
    private int mCurrentChapter;
    private int mWordCount;
    private String mSource;

    private static final long serialVersionUID = 2492120520485903037L;

    public Story(){
        mAuthors = new ArrayList<String>();
        mTitle = "Untitled";
        mChapters = new ArrayList<Chapter>();
        mStoryId = "000000";
        mCurrentChapter = 1;
    }

    public Story(String mTitle, ArrayList<String> mAuthors, ArrayList<String> mFandoms, String mStoryId, int mWordCount) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mFandoms = mFandoms;
        this.mStoryId = mStoryId;
        this.mChapters = new ArrayList<Chapter>();
        this.mCurrentChapter = 1;
        this.mWordCount = mWordCount;
    }

    public Story(String mTitle, String mSummary, ArrayList<String> mAuthors, ArrayList<Chapter> mChapters, ArrayList<String> mFandoms, String mStoryId, int mWordCount, String mSource) {
        this.mTitle = mTitle;
        this.mSummary = mSummary;
        this.mAuthors = mAuthors;
        this.mFandoms = mFandoms;
        this.mStoryId = mStoryId;
        this.mChapters = mChapters;
        this.mCurrentChapter = 1;
        this.mWordCount = mWordCount;
        this.mSource = mSource;
    }
    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSummary(){
        return mSummary;
    }

    public int getSize(){
        return mChapters.size();
    }

    public int getWordCount(){
        return mWordCount;
    }

    public String getStoryId() {
        return mStoryId;
    }

    public ArrayList<String> getFandoms() {
        return mFandoms;
    }

    public ArrayList<Chapter> getChapters() {
        return mChapters;
    }

    public Chapter getChapter(int chapter) {
        // chapter is a number between 1-n, eg chapter 1, chapter 2...
        if (chapter <= getSize() && chapter > 0) {
            return mChapters.get(chapter - 1);
        } else {
            return null;
        }
    }

    public Chapter getCurrentChapter(){
        return getChapter(mCurrentChapter);
    }

    public Chapter getNextChapter(){
        mCurrentChapter += 1;
        return getChapter(mCurrentChapter);
    }

    public Chapter getPrevChapter(){
        mCurrentChapter -= 1;
        return getChapter(mCurrentChapter);
    }

    public String getSource() {
        return mSource;
    }

    public String toString(){
        String story = String.format("%s by %s: %d words\n%s", getTitle(), getAuthors(), getWordCount(), getFandoms());
        return story;
    }
}
