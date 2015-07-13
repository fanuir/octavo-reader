package com.fanuir.octavoreader;

import java.io.Serializable;

/**
 * Created by ivy on 7/10/15.
 */
public class Chapter implements Serializable {

    private String mTitle;
    private String mNotes;
    private String mContent;

    public Chapter() {
        this.mTitle = "Chapter 1";
        this.mNotes = "A new chapter.";
        this.mContent = "<p>It was a wondrous day.</p>";
    }

    public Chapter(String mTitle, String mNotes, String mContent) {
        this.mTitle = mTitle;
        this.mNotes = mNotes;
        this.mContent = mContent;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getNotes() {
        return mNotes;
    }

    public String getContent() {
        return mContent;
    }

    public String toString(){
        String chapter = String.format("%s", getTitle());
        return chapter;
    }
}
