package com.fanuir.octavoreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by ivy on 7/10/15.
 */
public class ArchiveStoryFetcher extends AsyncTask<String, Void, Void>{

    private Context mContext;
    private Story mStory;
    private ProgressDialog progressDialog;

    public ArchiveStoryFetcher(Context context){
        super();
        mContext = context;
    }

    @Override
    protected void onPreExecute(){
        progressDialog = ProgressDialog.show(mContext, "Downloading...", "Please wait.");
    }

    @Override
    protected Void doInBackground(String... params) {
        String storyId = params[0];
        mStory = ArchiveStoryUtils.getStory(storyId);
        StoryData storyData = ArchiveStoryUtils.getStoryMetadata(storyId);
        //Write story to file here
        //System.out.println(mContext.getFilesDir());
        ArchiveStoryUtils.saveStory(mContext, mStory);
        ArchiveStoryUtils.saveMetadata(mContext, storyData);
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
