package com.fanuir.octavoreader;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ivy on 7/24/15.
 */
public class LibraryUtils {

    public static ArrayList<JsonObject> getStoryList(Context context){

        ArrayList<JsonObject> list = new ArrayList<>();
        JsonArray metadata;
        JsonParser jsonParser = new JsonParser();

        try {
            FileInputStream fis = context.openFileInput(Constants.STORY_METADATA_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            metadata = (JsonArray) jsonParser.parse((String) is.readObject());

        } catch (Exception e){
            metadata = new JsonArray();
            e.printStackTrace();
        }

        for(int i = 0; i < metadata.size(); i++){
            list.add((JsonObject) metadata.get(i));
        }

        sortStoryList(list);

        return list;
    }

    public static void sortStoryList(List<JsonObject> stories){
        Collections.sort(stories, new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject lhs, JsonObject rhs) {
                Date first = new Date(lhs.get("last_opened").getAsLong());
                Date second = new Date(rhs.get("last_opened").getAsLong());
                return first.compareTo(second);
            }
        });
        Collections.reverse(stories);
        System.out.println("Sorted list.");
    }

    public static String printJsonArray(JsonArray jsonArray){
        String items = "";
        for(int i = 0; i < jsonArray.size(); i++){
            items += jsonArray.get(i).getAsString();
            if(i != jsonArray.size() - 1){
                items += ", ";
            }
        }
        return items;
    }

    public static CharSequence trim(CharSequence s) {
        int start = 0;
        int end = s.length();
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }

}
