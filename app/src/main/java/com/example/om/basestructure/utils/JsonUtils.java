package com.example.om.basestructure.utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by android on 19.11.17.
 */

public class JsonUtils {

    public static final String LOG_TAG = JsonUtils.class.getSimpleName();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] objectToJson(Object o) {
        Log.d(LOG_TAG, "objectToJson() is called");
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    public static Object jsonToObject(byte[] json, Class clazz) {
        Log.d(LOG_TAG, "jsonToObject() is called");
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    public static Map<String, Object> objectToMap(Object o) {
        Log.d(LOG_TAG, "objectToMap() is called");
        byte[] json = objectToJson(o);
        return (Map<String, Object>) jsonToObject(json, Map.class);
    }
}
