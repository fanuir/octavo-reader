package com.fanuir.octavoreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.JsonObject;

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

        final GestureDetector gestureDetector = new GestureDetector(LibraryActivity.this, new LibraryGestureListener());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        mList.setOnTouchListener(gestureListener);

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

    public void onSwipeLeft(int position){
        //Toast.makeText(LibraryActivity.this, "Swiped left.", Toast.LENGTH_SHORT).show();
        View mainView = mList.getChildAt(position).findViewById(R.id.story_item_view_main);

        ViewSwitcher viewSwitcher = (ViewSwitcher) mList.getChildAt(position).findViewById(R.id.story_item_view_switcher);
        if(viewSwitcher.getCurrentView() != mainView) {
            Animation animIn = AnimationUtils.loadAnimation(LibraryActivity.this, R.anim.slide_in_right);
            Animation animOut = AnimationUtils.loadAnimation(LibraryActivity.this, R.anim.slide_out_left);
            viewSwitcher.setInAnimation(animIn);
            viewSwitcher.setOutAnimation(animOut);
            viewSwitcher.showPrevious();
        }
    }

    public void onSwipeRight(int position){
        //Toast.makeText(LibraryActivity.this, "Swiped right.", Toast.LENGTH_SHORT).show();
        View moreView = mList.getChildAt(position).findViewById(R.id.story_item_view_more);
        ViewSwitcher viewSwitcher = (ViewSwitcher) mList.getChildAt(position).findViewById(R.id.story_item_view_switcher);
        if(viewSwitcher.getCurrentView() != moreView) {
            Animation animIn = AnimationUtils.loadAnimation(LibraryActivity.this, R.anim.slide_in_left);
            Animation animOut = AnimationUtils.loadAnimation(LibraryActivity.this, R.anim.slide_out_right);
            viewSwitcher.setInAnimation(animIn);
            viewSwitcher.setOutAnimation(animOut);
            viewSwitcher.showNext();
        }
    }

    public void onItemClick(int adapterIndex, int viewPos){
        ViewSwitcher viewSwitcher = (ViewSwitcher) mList.getChildAt(viewPos).findViewById(R.id.story_item_view_switcher);
        View mainView = mList.getChildAt(viewPos).findViewById(R.id.story_item_view_main);
        if(viewSwitcher.getCurrentView() == mainView) {
            JsonObject story = mStoryList.get(adapterIndex);
            String storyId = story.get("id").getAsString();
            Intent intent = ReaderActivity.newInstance(LibraryActivity.this);
            intent.putExtra("id", storyId);
            startActivity(intent);
        } else {
            System.out.println("View pos: " + viewPos);
            System.out.println("Second view clicked.");
        }
    }

    class LibraryGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onSingleTapUp(MotionEvent e){
            System.out.println("X: " + e.getX() + "Y: " + e.getY());
            int adapterIndex = mList.pointToPosition((int)e.getX(), (int)e.getY());
            int visibleIndex = mList.getFirstVisiblePosition();
            int pos = adapterIndex - visibleIndex;
            onItemClick(adapterIndex, pos);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY){
            try {
                if (e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE
                        && Math.abs(e1.getY() - e2.getY()) < Constants.SWIPE_HORIZONTAL_Y_MAX
                        && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
                    int adapterIndex = mList.pointToPosition((int)e1.getX(), (int)e1.getY());
                    int visibleIndex = mList.getFirstVisiblePosition();
                    int pos = adapterIndex - visibleIndex;
                    onSwipeLeft(pos);
                } else if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE
                        && Math.abs(e1.getY() - e2.getY()) < Constants.SWIPE_HORIZONTAL_Y_MAX
                        && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
                    int adapterIndex = mList.pointToPosition((int)e1.getX(), (int)e1.getY());
                    int visibleIndex = mList.getFirstVisiblePosition();
                    int pos = adapterIndex - visibleIndex;
                    onSwipeRight(pos);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }
    }

}
