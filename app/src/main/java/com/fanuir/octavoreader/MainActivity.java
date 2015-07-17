package com.fanuir.octavoreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button mGetStoryButton;
    Button mReaderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGetStoryButton = (Button) findViewById(R.id.get_story_button);
        mGetStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idField = (EditText) findViewById(R.id.story_id_field);
                String storyId = idField.getText().toString();
                try {
                    int i = Integer.parseInt(storyId);
                    ArchiveStoryFetcher sf = new ArchiveStoryFetcher(v.getContext());
                    sf.execute(storyId);
                } catch(NumberFormatException e) {
                    //e.printStackTrace();
                    Toast.makeText(v.getContext(), "Input must be numeric.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mReaderButton = (Button) findViewById(R.id.reader_button);
        mReaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idField = (EditText) findViewById(R.id.story_id_field);
                String storyId = idField.getText().toString();

                Intent intent = ReaderActivity.newInstance(MainActivity.this);
                intent.putExtra("storyId", storyId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSettings(){
        Intent intent = SettingsActivity.newInstance(MainActivity.this);
        startActivity(intent);
    }
}
