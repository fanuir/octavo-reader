package com.fanuir.octavoreader;

import java.util.Date;

/**
 * Created by ivy on 7/10/15.
 */
public class Chapter {

    private String mTitle;
    private Date mDatePublished;
    private Date mDateUpdated;
    private String mDescription;
    private String mContent;

    public Chapter() {
        this.mTitle = "Chapter 1";
        this.mDatePublished = new Date(System.currentTimeMillis());
        this.mDescription = "A new chapter.";
        this.mContent = "<p>It was a wondrous day.</p>";
    }

    public Chapter(String mTitle, Date mDatePublished, Date mDateUpdated, String mDescription, String mContent) {
        this.mTitle = mTitle;
        this.mDatePublished = mDatePublished;
        this.mDateUpdated = mDateUpdated;
        this.mDescription = mDescription;
        this.mContent = mContent;
    }
}
