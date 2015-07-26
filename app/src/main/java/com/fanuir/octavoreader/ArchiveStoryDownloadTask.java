package com.fanuir.octavoreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by ivy on 7/10/15.
 */
public class ArchiveStoryDownloadTask extends AsyncTask<String, Void, Void>{

    private Context mContext;
    private Story mStory;
    private ProgressDialog progressDialog;

    public ArchiveStoryDownloadTask(Context context){
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
        mStory = ArchiveStoryUtils.downloadStory(storyId);
        //Write story to file here
        ArchiveStoryUtils.saveChaptersToFile(mContext, mStory.getChapters(), mStory.getId());
        ArchiveStoryUtils.saveMetadataToFile(mContext, mStory.getMetadata());
        return null;
    }

    protected void onPostExecute(Void param){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(mStory != null) {
            Toast.makeText(mContext, mStory.toString(), Toast.LENGTH_LONG).show();
            //Intent i = ReaderActivity.newInstance(mContext);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.putExtra("filename", mStory.());
            //mContext.startActivity(i);
        } else {
            Toast.makeText(mContext, "Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
