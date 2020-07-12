package com.example.myapplication.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static JSONObject stringToJsonObject(String string) throws JSONException {
        return new JSONObject(string);
    }
}
