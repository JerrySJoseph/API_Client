package com.jstechnologies.androidpostman.Helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class TypeConverter {

    public static String getJsonFromMap(Map<String,String> map)
    {
        Gson gson = new Gson();
        Type gsonType = new TypeToken<Map<String, String>>(){}.getType();
        return gson.toJson(map, gsonType);
    }
    public static Map<String,String> getMapFromJson(String json)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> myMap = gson.fromJson(json, type);
        return myMap;
    }
}
