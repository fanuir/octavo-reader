package com.fanuir.octavoreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private String[] mDrawerMenuItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerMenuItems = getResources().getStringArray(R.array.drawer_menu_choices);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_menu);

        mDrawerList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.drawer_list_item, mDrawerMenuItems));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Clicked drawer item.");
            }
        });

        Button mGetStoryButton = (Button) findViewById(R.id.get_story_button);
        mGetStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idField = (EditText) findViewById(R.id.story_id_field);
                String storyId = idField.getText().toString();
                ArchiveStoryDownloadTask sf = new ArchiveStoryDownloadTask(v.getContext());
                if(!storyId.equals("")) {
                    try {
                        sf.execute(storyId);
                        idField.setText("");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(v.getContext(), "Input must be numeric.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Please input an id.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button mLibraryButton = (Button) findViewById(R.id.library_button);
        mLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LibraryActivity.newInstance(MainActivity.this);
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
