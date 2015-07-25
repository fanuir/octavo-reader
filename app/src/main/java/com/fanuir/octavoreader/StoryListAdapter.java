package com.fanuir.octavoreader;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by ivy on 7/24/15.
 */
public class StoryListAdapter extends BaseAdapter {

    private List<JsonObject> mStoryList;
    private final Context mContext;

    public StoryListAdapter(Context context, List<JsonObject> mStoryList){
        this.mContext = context;
        this.mStoryList = mStoryList;
    }

    @Override
    public JsonObject getItem(int index){
        return mStoryList.get(index);
    }

    @Override
    public long getItemId(int index){
        return index;
    }

    @Override
    public int getCount(){
        return mStoryList.size();
    }

    private static class ViewHolder {
        public final TextView titleView;
        public final TextView idView;
        public final TextView summaryView;
        public final TextView authorView;

        public ViewHolder(TextView titleView, TextView idView, TextView authorView, TextView summaryView ){
            this.titleView = titleView;
            this.idView = idView;
            this.summaryView = summaryView;
            this.authorView = authorView;
        }
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent){

        TextView titleView;
        TextView idView;
        TextView authorView;
        TextView summaryView;

        if (convertView == null){
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.story_layout, parent, false);

            titleView = (TextView) convertView.findViewById(R.id.layout_story_title);
            idView = (TextView) convertView.findViewById(R.id.layout_story_id);
            summaryView = (TextView) convertView.findViewById(R.id.layout_story_summary);
            authorView = (TextView) convertView.findViewById(R.id.layout_story_authors);

            convertView.setTag(new ViewHolder(titleView, idView, authorView, summaryView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            titleView = viewHolder.titleView;
            idView = viewHolder.idView;
            authorView = viewHolder.authorView;
            summaryView = viewHolder.summaryView;
        }

        JsonObject story = getItem(index);
        System.out.println(story);
        CharSequence title = LibraryUtils.trim(Html.fromHtml(String.format("<h4>%s</h4>", story.get("title").getAsString())));
        String authors = "";
        JsonArray authorList = story.get("authors").getAsJsonArray();
        for(int i = 0; i < authorList.size(); i++){
            authors += authorList.get(i).getAsString();
            if(i != authorList.size() - 1){
                authors += ", ";
            }
        }

        titleView.setText(title);
        idView.setText(story.get("id").getAsString());
        authorView.setText(authors);
        summaryView.setText(story.get("summary").getAsString());

        return convertView;
    }

}
