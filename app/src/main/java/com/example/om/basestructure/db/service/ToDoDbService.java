package com.example.om.basestructure.db.service;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.TransactionalTask;
import com.example.om.basestructure.model.ServerToDo;
import com.example.om.basestructure.model.ToDo;
import com.example.om.basestructure.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by android on 18.01.18.
 */

public class ToDoDbService {

    private static final String LOG_TAG = ToDoDbService.class.getSimpleName();
    private static ToDoDbService instance;
    private DbService dbService = DbService.getInstance();

    private ToDoDbService() {

    }

    public static ToDoDbService getInstance() {
        if (instance == null) {
            instance = new ToDoDbService();
        }
        return instance;
    }

    public String saveToDo(ToDo toDo) {
        Log.d(LOG_TAG, "saveToDo() is called");
        byte[] json = JsonUtils.objectToJson(toDo);
        HashMap<String, Object> toDoProperties = (HashMap<String, Object>) JsonUtils.jsonToObject(json, Map.class);
        try {
            return dbService.saveDocument(toDo.getId(), toDoProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public ToDo getTodoById(String id) {
        return (ToDo) JsonUtils.jsonToObject(JsonUtils.objectToJson(dbService.getById(id)), ToDo.class);
    }

    public void saveToDos(final List<ServerToDo> toDos) {
        dbService.getDb().runInTransaction(new TransactionalTask() {
            @Override
            public boolean run() {
                try {
                    for (ServerToDo toDo : toDos) {
                        byte[] json = JsonUtils.objectToJson(toDo);
                        HashMap<String, Object> toDoProperties = (HashMap<String, Object>) JsonUtils.jsonToObject(json, Map.class);
                        dbService.saveDocument(null, toDoProperties);
                    }
                } catch (CouchbaseLiteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });
    }
}
