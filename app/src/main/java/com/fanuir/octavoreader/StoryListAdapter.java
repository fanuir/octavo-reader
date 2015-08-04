package com.fanuir.octavoreader;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.NumberFormat;
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

    public List<JsonObject> getData(){
        return mStoryList;
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
        public final TextView infoView;

        public ViewHolder(TextView titleView, TextView idView, TextView authorView, TextView summaryView, TextView infoView ){
            this.titleView = titleView;
            this.idView = idView;
            this.summaryView = summaryView;
            this.authorView = authorView;
            this.infoView = infoView;
        }
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent){

        TextView titleView;
        TextView idView;
        TextView authorView;
        TextView summaryView;
        TextView infoView;

        if (convertView == null || convertView.getTag() == null){
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.story_layout, parent, false);

            titleView = (TextView) convertView.findViewById(R.id.layout_story_title);
            idView = (TextView) convertView.findViewById(R.id.layout_story_id);
            summaryView = (TextView) convertView.findViewById(R.id.layout_story_summary);
            authorView = (TextView) convertView.findViewById(R.id.layout_story_author_line);
            infoView = (TextView) convertView.findViewById(R.id.layout_story_info);

            convertView.setTag(new ViewHolder(titleView, idView, authorView, summaryView, infoView));

        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            titleView = viewHolder.titleView;
            idView = viewHolder.idView;
            authorView = viewHolder.authorView;
            summaryView = viewHolder.summaryView;
            infoView = viewHolder.infoView;
        }

        JsonObject story = getItem(index);
        //System.out.println(story);
        CharSequence title = LibraryUtils.trim(Html.fromHtml(String.format("<h5>%s</h5>", story.get("title").getAsString())));
        String authors = LibraryUtils.printJsonArray(story.get("authors").getAsJsonArray());
        String fandoms = LibraryUtils.printJsonArray(story.get("fandoms").getAsJsonArray());
        String totalChapters = (story.get("total_chapters").getAsInt() == -1) ? "?" : story.get("total_chapters").toString();
        String authorLine = String.format("%s · <i>%s</i>", authors, fandoms);
        String infoLine = String.format("<b>Chapters:</b> %d/%s <b>Words:</b> %s",
                story.get("avail_chapters").getAsInt(), totalChapters,
                NumberFormat.getIntegerInstance().format(story.get("word_count").getAsInt()));

        titleView.setText(title);
        idView.setText(story.get("id").getAsString());
        authorView.setText(Html.fromHtml(authorLine));
        summaryView.setText(story.get("summary").getAsString());
        infoView.setText(Html.fromHtml(infoLine));

        return convertView;
    }

}
