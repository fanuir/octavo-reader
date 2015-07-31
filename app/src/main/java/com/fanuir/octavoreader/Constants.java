package com.fanuir.octavoreader;

/**
 * Created by ivy on 7/16/15.
 */
public class Constants {

    public static final String STORY_METADATA_FILENAME = "storyMetadata.json";
    public static final String ARCHIVE_PREFIX = "ao3";

    /* ARCHIVE SELECTORS */
    public static final String SEL_ARCHIVE_TITLE = "h2.title";
    public static final String SEL_ARCHIVE_SUMMARY = "div.summary blockquote";
    public static final String SEL_ARCHIVE_AUTHOR = "h3 a.author";
    public static final String SEL_ARCHIVE_WORD_COUNT = "dt:contains(Words) + dd";
    public static final String SEL_ARCHIVE_CHAPTER_STATS = "dt:contains(Chapters) + dd";
    public static final String SEL_ARCHIVE_FANDOMS = "dt.fandom + dd li";
    public static final String SEL_ARCHIVE_WARNINGS = "dt.warning + dd li";
    public static final String SEL_ARCHIVE_CATEGORIES = "dt.category + dd li";
    public static final String SEL_ARCHIVE_CHARACTERS = "dt.character + dd li";
    public static final String SEL_ARCHIVE_TAGS = "dt.freeform + dd li";
    public static final String SEL_ARCHIVE_RATING = "dt.rating + dd li";
    public static final String SEL_ARCHIVE_RELATIONSHIPS = "dt.relationship + dd li";
    public static final String SEL_ARCHIVE_NEXT_CHAPTER_LINK = "li.chapter.next a";
    public static final String SEL_ARCHIVE_CHAPTER_TITLE = "div.chapter h3.title";
    public static final String SEL_ARCHIVE_CHAPTER_NOTES = "div:not(.end).notes blockquote";
    public static final String SEL_ARCHIVE_CHAPTER_ENDNOTES = "div.end.notes blockquote";
    public static final String SEL_ARCHIVE_CHAPTER_TEXT = "div[role=article]";
    public static final String SEL_ARCHIVE_KUDOS = "dt + dd.kudos";
    public static final String SEL_ARCHIVE_BOOKMARKS = "dt + dd.bookmarks";
    public static final String SEL_ARCHIVE_HITS = "dt + dd.hits";
    public static final String SEL_ARCHIVE_PUBLISHED = "dt + dd.published";
    public static final String SEL_ARCHIVE_LAST_UPDATED = "dt + dd.status";

    public static final float SWIPE_MIN_DISTANCE = 120;
    public static final int SWIPE_THRESHOLD_VELOCITY = 200;
    public static final float SWIPE_HORIZONTAL_Y_MAX = 100;
}
