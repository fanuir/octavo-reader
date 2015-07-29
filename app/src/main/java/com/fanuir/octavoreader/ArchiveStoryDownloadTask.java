package com.fanuir.octavoreader;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by ivy on 7/10/15.
 */
public class ArchiveStoryDownloadTask extends AsyncTask<String, Integer, Integer>{

    private Context mContext;
    private Story mStory;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private int mMax;
    private int mId;

    public ArchiveStoryDownloadTask(Context context){
        super();
        mContext = context;
        mId = 1;
    }

    @Override
    protected void onPreExecute(){
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                .setContentTitle("Downloading...")
                .setContentText("Please wait.");
        Intent resultIntent = LibraryActivity.newInstance(mContext);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(LibraryActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        mMax = 0;
        mId++;

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    protected Integer doInBackground(String... params) {
        String storyId = params[0];
        int id = Integer.parseInt(storyId);
        mStory = ArchiveStoryUtils.downloadStory(this, storyId);
        //Write story to file here
        if(mStory != null) {
            ArchiveStoryUtils.saveChaptersToFile(mContext, mStory.getChapters(), mStory.getId());
            ArchiveStoryUtils.saveMetadataToFile(mContext, mStory.getMetadata());
        }
        return id;
    }

    @Override
    protected void onPostExecute(Integer param){
        mBuilder.setContentText("Download complete.")
                .setProgress(0, 0,false);
        mNotificationManager.notify(param, mBuilder.build());
        if(mStory != null) {
            Toast.makeText(mContext, mStory.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public NotificationManager getNotificationManager(){
        return mNotificationManager;
    }

    public NotificationCompat.Builder getBuilder(){
        return mBuilder;
    }

    public void setMax(int max){
        mMax = max;
    }

    public void doProgress(Integer... value){
        publishProgress(value);
    }

    @Override
    protected void onProgressUpdate(Integer... progress){
        if (mMax == 0) {
            mBuilder.setProgress(0, 0, true);
            mBuilder.setContentText("Downloading story...");
            mNotificationManager.cancel(1);
            mNotificationManager.notify(progress[1], mBuilder.build());
        } else {

            mBuilder.setProgress(mMax, progress[0], false);
            mBuilder.setContentText("Downloading chapter " + progress[0].toString() + " of " + mMax);
            mNotificationManager.notify(progress[1], mBuilder.build());
        }
    }
}
