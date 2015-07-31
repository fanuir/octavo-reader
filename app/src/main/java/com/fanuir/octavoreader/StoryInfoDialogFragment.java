package com.fanuir.octavoreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.text.NumberFormat;

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
        final JsonObject story = (JsonObject) jsonParser.parse(getArguments().getString("story"));

        builder.setTitle(story.get("title").getAsString())
                .setView(inflater.inflate(R.layout.dialog_story_info, null))
                .setPositiveButton(R.string.dialog_story_read, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = ReaderActivity.newInstance(getActivity());
                        intent.putExtra("id", story.get("id").getAsString());
                        startActivity(intent);
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
        String details = "";
        String[] tags = {"authors", "fandoms",  "warnings", "categories", "relationships", "characters", "tags"};
        String[] properties = {"summary"};
        String[] stats = {"avail_chapters", "word_count", "kudos", "hits", "bookmarks"};

        for (String tag : tags) {
            details += safeAddJsonArray(story, tag);
        }
        for (String property : properties){
            details += safeAddJsonString(story, property);
        }
        for (String stat : stats){
            details += safeAddJsonInt(story, stat);
        }

        detailsView.setText(Html.fromHtml(details));
    }

    public String safeAddJsonArray(JsonObject story, String property) {
        String list = LibraryUtils.printJsonArray(story.get(property).getAsJsonArray());
        if (!list.equals("")) {
            String desc = property.substring(0, 1).toUpperCase() + property.substring(1);
            return String.format("<p><b>%s: </b>%s</p>", desc ,list);
        } else {
            return "";
        }
    }

    public String safeAddJsonString(JsonObject story, String property) {
        String item = story.get(property).getAsString();
        if (!item.equals("")) {
            String desc = property.substring(0, 1).toUpperCase() + property.substring(1);
            return String.format("<p><b>%s: </b>%s</p>", desc, item);
        } else {
            return "";
        }
    }

    public String safeAddJsonInt(JsonObject story, String property){
        String item = NumberFormat.getIntegerInstance().format(story.get(property).getAsInt());
        if (!item.equals("")) {
            String desc;
            if(property.equals("avail_chapters")){
                desc = "Chapters";
                String total = (story.get("total_chapters").getAsInt() == -1) ? "?" : story.get("total_chapters").getAsString();
                item = String.format("%s/%s", item, total);
            } else if(property.equals("word_count")) {
                desc = "Words";
            } else {
                desc = property.substring(0, 1).toUpperCase() + property.substring(1);
            }
            return String.format("<p><b>%s: </b>%s</p>", desc, item);
        } else {
            return "";
        }
    }

}
