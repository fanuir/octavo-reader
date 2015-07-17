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
    public static final String SEL_ARCHIVE_NEXT_CHAPTER_LINK = "li.chapter.next a";
    public static final String SEL_ARCHIVE_CHAPTER_TITLE = "div.chapter h3.title";
    public static final String SEL_ARCHIVE_CHAPTER_NOTES = "div.notes blockquote";
    public static final String SEL_ARCHIVE_CHAPTER_ENDNOTES = "div.end.notes blockquote";
    public static final String SEL_ARCHIVE_CHAPTER_TEXT = "div[role=article]";


}