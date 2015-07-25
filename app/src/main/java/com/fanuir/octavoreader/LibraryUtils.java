package com.fanuir.octavoreader;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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

        return list;
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
