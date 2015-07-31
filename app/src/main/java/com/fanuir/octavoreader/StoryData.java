package com.fanuir.octavoreader;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ivy on 7/16/15.
 */
public class StoryData {

    public String id;

    public String source;

    public String title;

    public String rating;

    public String url;

    public ArrayList<String> warnings;

    public ArrayList<String> categories;

    public ArrayList<String> relationships;

    public ArrayList<String> characters;

    public ArrayList<String> tags;

    public ArrayList<String> authors;

    public ArrayList<String> fandoms;

    @SerializedName("user_bookmarks")
    public ArrayList<String> userBookmarks;

    @SerializedName("avail_chapters")
    public int availChapters;

    @SerializedName("total_chapters")
    public int totalChapters;

    @SerializedName("current_chapter")
    public int currentChapter;

    public String status;

    public boolean following;

    @SerializedName("last_updated")
    public String lastUpdated;

    public String published;

    @SerializedName("word_count")
    public int wordCount;

    public int kudos;

    public int bookmarks;

    public int hits;

    @SerializedName("last_opened")
    public long lastOpened;

    @SerializedName("last_synced")
    public long lastSynced;

    @SerializedName("last_position")
    public int lastPosition;

    public boolean read;

    public String summary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setFandoms(ArrayList<String> fandoms) {
        this.fandoms = fandoms;
    }

    public int getAvailChapters() {
        return availChapters;
    }

    public void setAvailChapters(int availChapters) {
        this.availChapters = availChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setKudos(int kudos) {
        this.kudos = kudos;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public void setLastOpened(long lastOpened) {
        this.lastOpened = lastOpened;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUserBookmarks(ArrayList<String> userBookmarks) {
        this.userBookmarks = userBookmarks;
    }

    public void setLastSynced(long lastSynced) {
        this.lastSynced = lastSynced;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setRelationships(ArrayList<String> relationships) {
        this.relationships = relationships;
    }

    public void setCharacters(ArrayList<String> characters) {
        this.characters = characters;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setWarnings(ArrayList<String> warnings) {
        this.warnings = warnings;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void setBookmarks(int bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
}
