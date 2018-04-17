package com.example.om.basestructure.db.view;

import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by android on 17.01.18.
 */

public class ToDoView implements Mapper {

    public static final String NAME = "toDo.list";

    @Override
    public void map(Map<String, Object> document, Emitter emitter) {
        if (document.get("type").equals("toDo")) {
            LinkedHashMap<String, Object> extracted = new LinkedHashMap<>();
            extracted.put("id", document.get("_id"));
            extracted.put("toDo", document.get("toDo"));
            extracted.put("description", document.get("description"));
            extracted.put("priority", document.get("priority"));
            extracted.put("endDate", document.get("endDate"));
            extracted.put("endTime", document.get("endTime"));
            extracted.put("sendNotification", document.get("sendNotification"));
            extracted.put("notificationId", document.get("notificationId"));
            extracted.put("location", document.get("location"));
            emitter.emit(document.get("_id"), extracted);
        }
    }
}
