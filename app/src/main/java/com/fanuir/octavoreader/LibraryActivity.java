package com.fanuir.octavoreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class LibraryActivity extends AppCompatActivity {

    List<JsonObject> mStoryList;
    StoryListAdapter mStoryAdapter;
    ListView mList;

    static public Intent newInstance(Context context) {
        return new Intent(context, LibraryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mStoryList = LibraryUtils.getStoryList(LibraryActivity.this);

        mList = (ListView) findViewById(R.id.library_list_view);
        TextView emptyView = (TextView) findViewById(R.id.library_empty);
        mList.setEmptyView(emptyView);

        mStoryAdapter = new StoryListAdapter(LibraryActivity.this, mStoryList);

        mList.setAdapter(mStoryAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonObject story = mStoryList.get(position);
                String storyId = story.get("id").getAsString();
                Intent intent = ReaderActivity.newInstance(LibraryActivity.this);
                intent.putExtra("id", storyId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = SettingsActivity.newInstance(LibraryActivity.this);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadAllData(){
        mStoryList = LibraryUtils.getStoryList(LibraryActivity.this);
        mStoryAdapter.getData().clear();
        mStoryAdapter.getData().addAll(mStoryList);
        mStoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        reloadAllData();
    }
}
