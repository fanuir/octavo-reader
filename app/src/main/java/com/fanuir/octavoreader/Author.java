package com.fanuir.octavoreader;

import java.util.ArrayList;

/**
 * Created by ivy on 7/10/15.
 */
public class Author {

    private String mName;
    private String mUrl;
    private ArrayList<Story> mStories;

    public Author(){
        mName = "Ida Belle";
        mUrl = "something";
        mStories = new ArrayList<Story>();
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public ArrayList<Story> getStories() {
        return mStories;
    }
}
