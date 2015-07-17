package com.fanuir.octavoreader;

import android.content.Context;
import android.text.Html;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by ivy on 7/16/15.
 */
public class ArchiveStoryUtils {

    public static void saveStory(Context context, Story story){
        String storyFile = story.getSource() + "-" + story.getStoryId();
        try {
            FileOutputStream fos = context.openFileOutput(storyFile, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(story);
            os.close();
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveMetadata(Context context, StoryData storyData){
        JsonObject metadata;
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        String json = gson.toJson(storyData);
        System.out.println(json);
        try {
            FileInputStream fis = context.openFileInput(Constants.STORY_METADATA_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            metadata = (JsonObject) jsonParser.parse((String) is.readObject());

        } catch (Exception e){
            metadata = new JsonObject();
            e.printStackTrace();
        }

        metadata.add(storyData.getFilename(), jsonParser.parse(json));
        String result = gson.toJson(metadata);
        System.out.println(result);

        try{
            FileOutputStream fos = context.openFileOutput(Constants.STORY_METADATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(result);
            System.out.println("Wrote to file.");
            os.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Story getStory(String id){

        Document fic;
        String url = String.format("http://archiveofourown.org/works/%s?view_adult=true",id);

        try {
            fic = Jsoup.connect(url).get();
            return parseStory(fic, id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StoryData getStoryMetadata(String id){

        Document fic;
        String url = String.format("http://archiveofourown.org/works/%s?view_adult=true",id);

        try {
            fic = Jsoup.connect(url).get();
            return parseStoryMetadata(fic, id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StoryData parseStoryMetadata(Document fic, String id){
        System.out.println("PARSING STORY WITH ID: " + id);
        /* Title */
        String title = fic.select(Constants.SEL_ARCHIVE_TITLE).text();

        String summary = fic.select(Constants.SEL_ARCHIVE_SUMMARY).text();

        /* Authors */
        ArrayList<String> as = new ArrayList<String>();
        Elements authors = fic.select(Constants.SEL_ARCHIVE_AUTHOR);
        for(Element author : authors){
            as.add(author.text());
        }

        /* Word count */
        int wordCount = Integer.parseInt(fic.select(Constants.SEL_ARCHIVE_WORD_COUNT).text());

        /* Total Chapters */
        String chaps = fic.select(Constants.SEL_ARCHIVE_CHAPTER_STATS).text();
        String[] chapData = chaps.split("/");
        int availChapters = Integer.parseInt(chapData[0]);
        int totalChapters;
        try {
            totalChapters = Integer.parseInt(chapData[1]);
        } catch (NumberFormatException e){
            totalChapters = -1;
        }
        /* Fandoms */
        ArrayList<String> fs = new ArrayList<String>();
        Elements fandoms = fic.select(Constants.SEL_ARCHIVE_FANDOMS);
        for(Element fandom : fandoms){
            fs.add(fandom.text());
        }

        StoryData metadata = new StoryData();

        metadata.setStoryId(id);
        metadata.setTitle(title);
        metadata.setAuthors(as);
        metadata.setFandoms(fs);
        metadata.setAvailChapters(availChapters);
        metadata.setTotalChapters(totalChapters);
        metadata.setDescription(summary);
        metadata.setWordCount(wordCount);
        metadata.setSource(Constants.ARCHIVE_PREFIX);

        return metadata;

    }

    public static Story parseStory(Document fic, String id){
        System.out.println("PARSING STORY WITH ID: " + id);
        /* Title */
        String title = fic.select(Constants.SEL_ARCHIVE_TITLE).text();

        String summary = fic.select(Constants.SEL_ARCHIVE_SUMMARY).text();

        /* Authors */
        ArrayList<String> as = new ArrayList<String>();
        Elements authors = fic.select(Constants.SEL_ARCHIVE_AUTHOR);
        for(Element author : authors){
            as.add(author.text());
        }

        /* Word count */
        int wordCount = Integer.parseInt(fic.select(Constants.SEL_ARCHIVE_WORD_COUNT).text());

        /* Total Chapters */
        String chaps = fic.select(Constants.SEL_ARCHIVE_CHAPTER_STATS).text();
        String[] chapData = chaps.split("/");
        int availChapters = Integer.parseInt(chapData[0]);
        int totalChapters;
        try {
            totalChapters = Integer.parseInt(chapData[1]);
        } catch (NumberFormatException e){
            totalChapters = -1;
        }
        /* Fandoms */
        ArrayList<String> fs = new ArrayList<String>();
        Elements fandoms = fic.select(Constants.SEL_ARCHIVE_FANDOMS);
        for(Element fandom : fandoms){
            fs.add(fandom.text());
        }

        ArrayList<Chapter> chapters = getChapters(fic, availChapters);
        Chapter first = chapters.get(0);
        String content = String.format("<h1>%s</h1>%s", title, first.getContent());
        first.setContent(content);

        return new Story(title, summary, as, chapters, fs, id, wordCount, "ao3");
    }


    public static ArrayList<Chapter> getChapters(Document fic, int numChaps){
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();

        Chapter first = getChapter(fic);
        chapters.add(first);
        System.out.println(numChaps);
        if(numChaps > 1) {
            /* Rest of the availChapters */
            Document nextChap;
            String nextUrl = String.format("http://archiveofourown.org%s?view_adult=true", fic.select(Constants.SEL_ARCHIVE_NEXT_CHAPTER_LINK).attr("href"));
            for (int i = 1; i < numChaps; i++) {
                try {
                    nextChap = Jsoup.connect(nextUrl).get();
                    chapters.add(getChapter(nextChap));
                    nextUrl = String.format("http://archiveofourown.org%s?view_adult=true", nextChap.select(Constants.SEL_ARCHIVE_NEXT_CHAPTER_LINK).attr("href"));
                    System.out.println(nextUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(chapters);
        return chapters;
    }

    public static Chapter getChapter(Document chap){
        String title = chap.select(Constants.SEL_ARCHIVE_CHAPTER_TITLE).text();
        String notes = null;
        String endnotes = null;
        try {
            notes = chap.select(Constants.SEL_ARCHIVE_CHAPTER_NOTES).first().html();
            endnotes = chap.select(Constants.SEL_ARCHIVE_CHAPTER_ENDNOTES).html();
        } catch (Exception e){
            System.out.println("No notes.");
        }

        // Replace "Chapter Text" with the chapter title and notes
        Element rawContent = chap.select(Constants.SEL_ARCHIVE_CHAPTER_TEXT).first();
        rawContent.select("h3").first().remove();

        //TODO: Remove image tags, or something.
        if(notes != null && !notes.equals("")){
            rawContent.prepend("<p>" + notes + "</p><p>---</p>");
        }
        if(endnotes != null && !endnotes.equals("")){
            String extra = String.format("<p>---</p><h3>End Notes:</h3><p>%s</p>",endnotes);
            rawContent.append(extra);
        }
        if(!title.equals("")){
            rawContent.prepend("<h3>" + title + "</h3>");
        }
        String content = rawContent.html();
        return new Chapter(title, notes, content);
    }
}
