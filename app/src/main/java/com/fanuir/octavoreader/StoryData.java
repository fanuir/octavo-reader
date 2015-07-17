package com.fanuir.octavoreader;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ivy on 7/16/15.
 */
public class StoryData {

    @SerializedName("story_id")
    public String storyId;

    public String source;

    public String title;

    public ArrayList<String> authors;

    public ArrayList<String> fandoms;

    @SerializedName("avail_chapters")
    public int availChapters;

    @SerializedName("total_chapters")
    public int totalChapters;

    public String status;

    @SerializedName("last_updated")
    public String lastUpdated;

    public String published;

    @SerializedName("word_count")
    public int wordCount;

    public int kudos;

    @SerializedName("last_opened")
    public long lastOpened;

    public String description;

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFilename(){
        return String.format("%s-%s", getSource(), getStoryId());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getFandoms() {
        return fandoms;
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

    public int getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getKudos() {
        return kudos;
    }

    public void setKudos(int kudos) {
        this.kudos = kudos;
    }

    public long getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(long lastOpened) {
        this.lastOpened = lastOpened;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
