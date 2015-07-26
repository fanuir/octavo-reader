package com.fanuir.octavoreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
            final String data;
            data = extras.getString("data");
            final JsonObject metadata = (JsonObject) new JsonParser().parse(data);
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
            Toast.makeText(ReaderActivity.this, "Loading Chapter...", Toast.LENGTH_SHORT).show();
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebReader.loadNextChapter();
                }
            });

            return true;
        } else if(id == R.id.action_prev_chap) {
            Toast.makeText(ReaderActivity.this, "Loading Chapter...", Toast.LENGTH_SHORT).show();

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebReader.loadPrevChapter();
                }
            });

            return true;
        } else if(id == R.id.action_bookmark) {
            WebReader reader = (WebReader) findViewById(R.id.web_reader);
            int pos = reader.getScrollY();

            System.out.println(String.format("Position: %d", pos));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        if(mWebReader != null){
            //save read state here
            System.out.println("Saved story progress.");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        if(mWebReader != null){
            //save read state here
            System.out.println("Restored story progress.");
        }
    }
}
