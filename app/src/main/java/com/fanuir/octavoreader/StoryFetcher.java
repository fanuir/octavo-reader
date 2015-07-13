package com.fanuir.octavoreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * Created by ivy on 7/10/15.
 */
public class StoryFetcher extends AsyncTask<String, Void, Void>{

    private Context mContext;
    private Story mStory;
    private ProgressDialog progressDialog;

    public StoryFetcher(Context context){
        super();
        mContext = context;
    }

    public Story getStory(String id){

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

    public Story parseStory(Document fic, String id){
        System.out.println("PARSING STORY WITH ID: " + id);
        /* Title */
        String title = fic.select("h2.title").text();

        String summary = fic.select("div.summary blockquote").text();

        /* Authors */
        ArrayList<String> as = new ArrayList<String>();
        Elements authors = fic.select("h3 a.author");
        for(Element author : authors){
            as.add(author.text());
        }

        /* Word count */
        int wordCount = Integer.parseInt(fic.select("dt:contains(Words) + dd").text());

        /* Total Chapters */
        String chaps = fic.select("dt:contains(Chapters) + dd").text();
        String[] chapData = chaps.split("/");
        int availChapters = Integer.parseInt(chapData[0]);
        int totalChapters = Integer.parseInt(chapData[1]);

        /* Fandoms */
        ArrayList<String> fs = new ArrayList<String>();
        Elements fandoms = fic.select("dt.fandom + dd li");
        for(Element fandom : fandoms){
            fs.add(fandom.text());
        }

        ArrayList<Chapter> chapters = getChapters(fic, availChapters);

        return new Story(title, summary, as, chapters, fs, id, wordCount);
    }

    public ArrayList<Chapter> getChapters(Document fic, int numChaps){
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();

        chapters.add(getChapter(fic));
        System.out.println(numChaps);

        if(numChaps > 1) {
            /* Rest of the chapters */
            Document nextChap;
            String nextUrl = "http://archiveofourown.org" + fic.select("li.chapter.next a").attr("href") + "?view_adult=true";
            for (int i = 1; i < numChaps; i++) {
                try {
                    nextChap = Jsoup.connect(nextUrl).get();
                    chapters.add(getChapter(nextChap));
                    nextUrl = "http://archiveofourown.org" + nextChap.select("li.chapter.next a").attr("href") + "?view_adult=true";
                    System.out.println(nextUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(chapters);
        return chapters;
    }

    public Chapter getChapter(Document chap){
        /* First Chapter */
        String title = chap.select("div.chapter h3.title").text();
        String notes = null;
        try {
            notes = chap.select("div.notes h3 + blockquote").text();
            System.out.println(notes);
        } catch (Exception e){
            e.printStackTrace();
        }

        // Replace "Chapter Text" with the chapter title and notes
        Element rawContent = chap.select("div[role=article]").first();
        rawContent.select("h3").first().remove();

        //TODO: Remove image tags, or something.
        if(notes != null || !notes.equals("")){
            rawContent.prepend("<p><i>" + notes + "</i></p><p>---</p>");
        }
        rawContent.prepend("<h3>" + title + "</h3>");
        String content = rawContent.html();
        return new Chapter(title, notes, content);
    }

    @Override
    protected void onPreExecute(){
        progressDialog = ProgressDialog.show(mContext, "Downloading...", "Please wait.");
    }

    @Override
    protected Void doInBackground(String... params) {
        String storyId = params[0];
        mStory = getStory(storyId);
        //Write story to file here
        System.out.println(mContext.getFilesDir());
        String fileName = "storyObject" + storyId + ".ser";
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(mStory);
            os.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void param){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(mStory != null) {
            Toast.makeText(mContext, mStory.toString(), Toast.LENGTH_LONG).show();
            Intent i = ReaderActivity.newInstance(mContext);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("storyId", mStory.getStoryId());
            mContext.startActivity(i);
        } else {
            Toast.makeText(mContext, "Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
