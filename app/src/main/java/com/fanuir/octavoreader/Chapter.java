package com.fanuir.octavoreader;

import java.io.Serializable;

/**
 * Created by ivy on 7/10/15.
 */
public class Chapter implements Serializable {

    private String mTitle;
    private String mContent;
    private String mUrl;

    public Chapter(String mTitle, String mContent, String mUrl) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getUrl(){
        return mUrl;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public void setUrl(String mUrl){
        this.mUrl = mUrl;
    }

    public String toString(){
        String chapter = String.format("%s", getTitle());
        return chapter;
    }
}
