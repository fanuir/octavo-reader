package com.fanuir.octavoreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

public class ReaderActivity extends AppCompatActivity {

    Reader mReader;
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
            final String STORY_ID;
            STORY_ID = extras.getString("storyId");
            mReader = (Reader) findViewById(R.id.reader_view);
            Toast.makeText(ReaderActivity.this, "Opening Story...", Toast.LENGTH_SHORT).show();
            uiHandler = new Handler();
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mReader.loadStory(STORY_ID);
                    setTitle(mReader.getStory().getTitle());
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
            return true;
        } else if(id == R.id.action_next_chap) {
            Toast.makeText(ReaderActivity.this, "Loading Chapter...", Toast.LENGTH_SHORT).show();
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mReader.loadNextChapter();
                }
            });


            ScrollView scroller = (ScrollView) findViewById(R.id.scroll_reader);
            scroller.scrollTo(0,0);

            return true;
        } else if(id == R.id.action_prev_chap) {
            Toast.makeText(ReaderActivity.this, "Loading Chapter...", Toast.LENGTH_SHORT).show();

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mReader.loadPrevChapter();
                }
            });

            ScrollView scroller = (ScrollView) findViewById(R.id.scroll_reader);
            scroller.scrollTo(0, 0);
            return true;
        } else if(id == R.id.action_bookmark) {
            Reader reader = (Reader) findViewById(R.id.reader_view);
            ScrollView scroller = (ScrollView) findViewById(R.id.scroll_reader);
            int pos = scroller.getScrollY();

            System.out.println(String.format("Position: %d", pos));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
