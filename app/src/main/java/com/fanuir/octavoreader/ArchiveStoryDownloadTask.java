package com.fanuir.octavoreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by ivy on 7/10/15.
 */
public class ArchiveStoryDownloadTask extends AsyncTask<String, Integer, Void>{

    private Context mContext;
    private Story mStory;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogInit;

    public ArchiveStoryDownloadTask(Context context){
        super();
        mContext = context;
    }

    @Override
    protected void onPreExecute(){
        progressDialogInit = ProgressDialog.show(mContext, "Downloading...", "Please wait.");
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage("Please wait.");
    }

    @Override
    protected Void doInBackground(String... params) {
        String storyId = params[0];
        mStory = ArchiveStoryUtils.downloadStory(this, storyId);
        //Write story to file here
        if(mStory != null) {
            ArchiveStoryUtils.saveChaptersToFile(mContext, mStory.getChapters(), mStory.getId());
            ArchiveStoryUtils.saveMetadataToFile(mContext, mStory.getMetadata());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void param){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(progressDialogInit.isShowing()){
            progressDialogInit.dismiss();
        }
        if(mStory != null) {
            Toast.makeText(mContext, mStory.toString(), Toast.LENGTH_LONG).show();
            //Intent i = ReaderActivity.newInstance(mContext);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.putExtra("filename", mStory.());
            //mContext.startActivity(i);
        } else {
            Toast.makeText(mContext, "Failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setMax(int max){
        progressDialog.setMax(max);
    }

    public void doProgress(int value){
        publishProgress(value);
    }

    @Override
    protected void onProgressUpdate(Integer... progress){
        progressDialog.setProgress(progress[0]);
        if (progressDialog.getMax() == 0){
            progressDialogInit.dismiss();
            progressDialog.show();
            progressDialog.setMessage("Downloading story...");
        } else {
            progressDialog.setMessage("Downloading chapter " + progress[0].toString() + " of " + progressDialog.getMax());
        }
    }
}
