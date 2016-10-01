package com.example.siddhi.contactsapp.helper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yogendra singh on 23-08-2016.
 */
public class Excpetion2JSON {
    public static JSONObject getJSON(Exception e) {
    try{
        JSONObject json = new JSONObject();
        json.put("result", -4);
        json.put("message", e.getMessage());
        return json;
    } catch(JSONException je) {
        return null;
    }
}
}
