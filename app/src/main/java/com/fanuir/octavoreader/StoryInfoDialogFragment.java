package com.fanuir.octavoreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ivy on 7/29/15.
 */
public class StoryInfoDialogFragment extends DialogFragment {

    JsonObject mStory;

    static StoryInfoDialogFragment newInstance(String story){
        StoryInfoDialogFragment fragment = new StoryInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("story", story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        JsonParser jsonParser = new JsonParser();
        JsonObject story = (JsonObject) jsonParser.parse(getArguments().getString("story"));

        builder.setTitle(story.get("title").getAsString())
                .setView(inflater.inflate(R.layout.dialog_story_info, null))
                .setPositiveButton(R.string.dialog_story_read, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Read story.");
                    }
                })
                .setNeutralButton(R.string.dialog_story_actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("More actions.");
                    }
                })
                .setNegativeButton(R.string.dialog_story_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart(){
        super.onStart();
        JsonParser jsonParser = new JsonParser();
        JsonObject story = (JsonObject) jsonParser.parse(getArguments().getString("story"));

        Dialog dialog = getDialog();

        TextView detailsView = (TextView) dialog.findViewById(R.id.dialog_story_info);
        String details = String.format("<p><b>Authors: </b>%s</p>", LibraryUtils.printJsonArray(story.get("authors").getAsJsonArray()));
        details += String.format("<p><b>Fandoms: </b>%s</p>", LibraryUtils.printJsonArray(story.get("fandoms").getAsJsonArray()));
        details += String.format("<p><b>Category: </b>%s</p>", LibraryUtils.printJsonArray(story.get("categories").getAsJsonArray()));
        details += String.format("<p><b>Relationships: </b>%s</p>", LibraryUtils.printJsonArray(story.get("relationships").getAsJsonArray()));
        details += String.format("<p><b>Characters: </b>%s</p>", LibraryUtils.printJsonArray(story.get("characters").getAsJsonArray()));
        details += String.format("<p><b>Tags: </b>%s</p>", LibraryUtils.printJsonArray(story.get("tags").getAsJsonArray()));
        details += String.format("<p><b>Summary: </b>%s</p>", story.get("summary").getAsString());
        detailsView.setText(Html.fromHtml(details));
    }

}
