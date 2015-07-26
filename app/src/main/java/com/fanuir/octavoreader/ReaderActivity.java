package com.fanuir.octavoreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class ReaderActivity extends AppCompatActivity {

    WebReader mWebReader;
    Handler uiHandler;

    static public Intent newInstance(Context context) {
        return new Intent(context, ReaderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String id;
            id = extras.getString("id");
            final JsonObject metadata = ArchiveStoryUtils.loadStoryMetadataFromFile(this, id);

            mWebReader = (WebReader) findViewById(R.id.web_reader);
            Toast.makeText(ReaderActivity.this, "Opening Story...", Toast.LENGTH_SHORT).show();
            uiHandler = new Handler();
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebReader.loadStory(metadata);
                    setTitle(mWebReader.getStory().getTitle());
                }
            });
            mWebReader.addJavascriptInterface(new WebReaderInterface(ReaderActivity.this),"Android");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = SettingsActivity.newInstance(ReaderActivity.this);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_next_chap) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebReader.loadNextChapter();
                }
            });
            return true;
        } else if(id == R.id.action_prev_chap) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebReader.loadPrevChapter();
                }
            });
            return true;
        } else if(id == R.id.action_bookmark) {
            float pos = mWebReader.calculateProgress();
            //add bookmark here
            System.out.println(String.format("Position: %f", pos));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mWebReader != null){
            mWebReader.saveStoryState();
            System.out.println("Saved story progress.");
        }
    }

    @Override
    public void onBackPressed(){
        mWebReader.saveStoryState();
        System.out.println("Back button pressed, saved.");
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if(mWebReader != null && mWebReader.getStory() != null){
            mWebReader.saveStoryState();
            System.out.println("Saved story progress.");
        }
    }

}
