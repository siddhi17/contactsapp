package com.example.siddhi.contactsapp.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Admin on 5/16/2016.
 */
public class Map2JSON {
    public JSONObject getJSON(Map<String, String> params) throws JSONException {
        JSONObject json = new JSONObject();
        Set<String> keySet= params.keySet();

        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            try {
                json.put(key, value);
            } catch(JSONException je) {
                je.printStackTrace();
                throw je;
            }
        } // end of while
        return json;
    }
}
