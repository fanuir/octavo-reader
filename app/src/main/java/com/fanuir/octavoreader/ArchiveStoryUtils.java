package com.fanuir.octavoreader;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import java.util.Date;

/**
 * Created by ivy on 7/16/15.
 */
public class ArchiveStoryUtils {

    public static Story downloadStory(String id){

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

        /* Kudos */
        int kudos = Integer.parseInt(fic.select(Constants.SEL_ARCHIVE_KUDOS).text());

        /* Published */
        String published = fic.select(Constants.SEL_ARCHIVE_PUBLISHED).text();

        /* Last updated */
        String updated;
        try {
            updated = fic.select(Constants.SEL_ARCHIVE_LAST_UPDATED).text();
        } catch (NullPointerException e){
            updated = published;
        }

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

        metadata.setId("ao3"+id);
        metadata.setTitle(title);
        metadata.setAuthors(as);
        metadata.setFandoms(fs);
        metadata.setAvailChapters(availChapters);
        metadata.setTotalChapters(totalChapters);
        metadata.setSummary(summary);
        metadata.setWordCount(wordCount);
        metadata.setSource(Constants.ARCHIVE_PREFIX);
        metadata.setCurrentChapter(1);
        metadata.setLastOpened(0L);
        metadata.setLastSynced(new Date().getTime());
        metadata.setKudos(kudos);
        metadata.setLastUpdated(updated);
        metadata.setPublished(published);

        return metadata;

    }

    public static Story parseStory(Document fic, String id){
        System.out.println("PARSING STORY WITH ID: " + id);

        StoryData metadata = parseStoryMetadata(fic, id);
        JsonObject data = metadataToJson(metadata);

        ArrayList<Chapter> chapters = getChapters(fic, metadata.getAvailChapters());
        Chapter first = chapters.get(0);
        String content = String.format("<h2>%s</h2>%s", metadata.getTitle(), first.getContent());
        first.setContent(content);

        return new Story(data, chapters);
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
        System.out.println(title);
        String notes = null;
        String endnotes = null;
        try {
            notes = chap.select(Constants.SEL_ARCHIVE_CHAPTER_NOTES).html();
            endnotes = chap.select(Constants.SEL_ARCHIVE_CHAPTER_ENDNOTES).html();
            System.out.println(endnotes);
        } catch (Exception e){
            System.out.println("No notes.");
            e.printStackTrace();
        }

        // Replace "Chapter Text" with the chapter title and notes
        Element rawContent = chap.select(Constants.SEL_ARCHIVE_CHAPTER_TEXT).first();
        rawContent.select("h3").first().remove();

        if(notes != null && !notes.equals("")){
            rawContent.prepend("<blockquote>" + notes + "</blockquote><hr>");
        }
        if(endnotes != null && !endnotes.equals("")){
            String extra = String.format("<hr><h3>End Notes:</h3><p>%s</p>",endnotes);
            rawContent.append(extra);
        }
        if(!title.equals("")){
            rawContent.prepend("<h3>" + title + "</h3>");
        }
        String content = rawContent.html();
        return new Chapter(title, notes, content);
    }

    public static void saveChaptersToFile(Context context, ArrayList<Chapter> chapters, String filename){
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(chapters);
            os.close();
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static JsonObject metadataToJson(StoryData metadata){
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        String json = gson.toJson(metadata);

        JsonObject data = (JsonObject) jsonParser.parse(json);
        return data;
    }

    public static JsonObject loadStoryMetadataFromFile(Context context, String id){
        JsonArray metadata;
        JsonParser jsonParser = new JsonParser();

        try {
            FileInputStream fis = context.openFileInput(Constants.STORY_METADATA_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            metadata = (JsonArray) jsonParser.parse((String) is.readObject());

        } catch (Exception e){
            metadata = new JsonArray();
            e.printStackTrace();
        }

        for(int i = 0; i < metadata.size(); i++){
            JsonObject curr = metadata.get(i).getAsJsonObject();
            if(curr.get("id").getAsString().equals(id)){
                return curr;
            }
        }
        return null;
    }

    public static void saveMetadataToFile(Context context, JsonObject data){
        JsonArray metadata;
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();

        try {
            FileInputStream fis = context.openFileInput(Constants.STORY_METADATA_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            metadata = (JsonArray) jsonParser.parse((String) is.readObject());

        } catch (Exception e){
            metadata = new JsonArray();
            e.printStackTrace();
        }

        addStoryToArray(metadata, data);

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

    public static void addStoryToArray(JsonArray jsonArray, JsonObject jsonObject){
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject curr = jsonArray.get(i).getAsJsonObject();
            if (curr.equals(jsonObject)) {
                return;
            }  else if(curr.get("title").getAsString().equals(jsonObject.get("title").getAsString())){
                jsonArray.remove(i);
                jsonArray.add(jsonObject);
                System.out.println("Story updated.");
                return;
            }
        }
        jsonArray.add(jsonObject);
        System.out.println("Story added.");
    }


}
