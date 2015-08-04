package com.fanuir.octavoreader;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ReaderActivity extends AppCompatActivity {

    LinearLayout mReaderLayout;
    WebReader mWebReader;
    Handler uiHandler;
    String mCurrHeaders;

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

            mReaderLayout = (LinearLayout) findViewById(R.id.reader_layout);
            mWebReader = new WebReader(ReaderActivity.this);
            mWebReader.setId(R.id.reader_view);
            mWebReader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mReaderLayout.addView(mWebReader);
            mCurrHeaders = mWebReader.getHeaders();

            Toast.makeText(ReaderActivity.this, "Opening Story...", Toast.LENGTH_SHORT).show();
            uiHandler = new Handler();
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebReader.loadStory(metadata);
                    setTitle(mWebReader.getStory().getTitle());
                }
            });
            mWebReader.addJavascriptInterface(new WebReaderInterface(ReaderActivity.this), "Android");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ReaderActivity.this);
            SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    System.out.println("Preferences changed.");
                    mWebReader.updateHeaders(ReaderActivity.this);
                    mWebReader.loadStoryState();
                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(mWebReader != null && mWebReader.getStory() != null){
            MenuItem prev = menu.findItem(R.id.action_prev_chap);
            MenuItem next = menu.findItem(R.id.action_next_chap);

            prev.setVisible(true);
            next.setVisible(true);

            if(mWebReader.isFirstChapter()){
                prev.setVisible(false);
            }
            if(mWebReader.isLastChapter()){
                next.setVisible(false);
            }
            if(mWebReader.getStory().getChapters().size() == 1){
                menu.removeItem(R.id.action_chapter_index);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent = SettingsActivity.newInstance(ReaderActivity.this);
                startActivity(intent);
                return true;
            case R.id.action_next_chap:
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebReader.loadNextChapter();
                    }
                });
                return true;
            case R.id.action_prev_chap:
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebReader.loadPrevChapter();
                    }
                });
                return true;
            case R.id.action_bookmark:
                float pos = mWebReader.calculateProgress();
                //add bookmark here
                System.out.println(String.format("Position: %f", pos));
                return true;
            case R.id.action_chapter_index:
                System.out.println("Show table of contents");
                if (mWebReader.getStory().getChapters().size() > 1) {
                    ChapterIndexDialogFragment chapterIndexDialog = new ChapterIndexDialogFragment();
                    chapterIndexDialog.show(getFragmentManager(), "chapterIndex");
                } else {
                    Toast.makeText(ReaderActivity.this, "Single chapter fic.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mWebReader != null){
            mWebReader.saveStoryState();
            System.out.println("Saved story progress.");
        }
        System.gc();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mWebReader != null){
            mWebReader.setFontSize(ReaderActivity.this);
            mWebReader.updateHeaders(ReaderActivity.this);
            if(!mWebReader.getHeaders().equals(mCurrHeaders)){
                System.out.println("Refresh.");
                mWebReader.loadStoryState();
                mCurrHeaders = mWebReader.getHeaders();
            }
        }
    }

    private void releaseWebView() {
        mReaderLayout.removeAllViews();
        if(mWebReader != null){
            mWebReader.setTag(null);
            mWebReader.clearHistory();
            mWebReader.removeAllViews();
            mWebReader.loadUrl("about:blank");
            mWebReader.destroy();
            mWebReader = null;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("Reader onDestroy called.");
        releaseWebView();
        System.gc();
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

    public static class ChapterIndexDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final WebReader reader = (WebReader) getActivity().findViewById(R.id.reader_view);
            final int curr = reader.getStory().getCurrentChapterNum() - 1;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ArrayList<Chapter> chapters = reader.getStory().getChapters();
            CharSequence[] chapterTitles = new CharSequence[chapters.size()];
            for(int i = 0; i < chapters.size(); i++){
                String title = chapters.get(i).getTitle();
                if(i == curr){
                    SpannableString bold = new SpannableString(title);
                    bold.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
                    chapterTitles[i] = bold;
                } else {
                    chapterTitles[i] = title;
                }
            }
            builder.setTitle("Chapter Index")
                    .setSingleChoiceItems(chapterTitles, curr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which != curr) {
                                reader.loadChapter(which);
                                dialog.dismiss();
                            }
                        }
                    });
            return builder.create();
        }

    }



}
