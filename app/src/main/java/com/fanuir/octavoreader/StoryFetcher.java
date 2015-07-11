package com.fanuir.octavoreader;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ivy on 7/10/15.
 */
public class StoryFetcher extends AsyncTask<String, Void, Void>{

    private Context mContext;
    private Story mStory;

    public StoryFetcher(Context context){
        super();
        mContext = context;
    }

    public static Story getStory(String id){

        Document fic;
        String url = "http://archiveofourown.org/works/" + id + "?view_adult=true";

        try {
            fic = Jsoup.connect(url).get();
            return parseStory(fic, id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Story parseStory(Document fic, String id){
        System.out.println("PARSING STORY WITH ID: " + id);
        /* Title */
        String title = fic.select("h2.title").text();

        /* Authors */
        ArrayList<String> as = new ArrayList<String>();
        Elements authors = fic.select("h3 a.author");
        for(Element author : authors){
            as.add(author.text());
        }

        /* Word count */
        int wordCount = Integer.parseInt(fic.select("dt:contains(Words) + dd").text());

        /* Total Chapters */
        String words = fic.select("dt:contains(Chapters) + dd").text();
        int totalChapters = Integer.parseInt(words.split("/")[1]);

        /* Fandoms */
        ArrayList<String> fs = new ArrayList<String>();
        Elements fandoms = fic.select("dt.fandom + dd li");
        for(Element fandom : fandoms){
            fs.add(fandom.text());
        }

        return new Story(title, as, fs, id, wordCount);
    }

    @Override
    protected Void doInBackground(String... params) {
        String storyId = params[0];
        mStory = getStory(storyId);

        return null;
    }

    protected void onPostExecute(Void param){
        if(mStory != null) {
            Toast.makeText(mContext, mStory.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
