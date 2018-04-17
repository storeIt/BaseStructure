package com.example.om.basestructure.db.service;


import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.ManagerOptions;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.QueryOptions;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.example.om.basestructure.db.QueryIterator;
import com.example.om.basestructure.db.model.DbObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by android on 12.11.17.
 */

public class DbService {

    private static final String LOG_TAG = DbService.class.getSimpleName();
    private static DbService instance;
    private Manager dbManager;
    private Database db;

    private DbService() {

    }

    public static DbService getInstance() {
        if (instance == null) {
            instance = new DbService();
        }
        return instance;
    }

    public void initDb(Context context) {
        try {
            dbManager = new Manager(new AndroidContext(context), new ManagerOptions());
            db = dbManager.getDatabase("basestructuredb");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public Database getDb() {
        return db;
    }

    public String saveDocument(String id, HashMap<String, Object> properties) throws CouchbaseLiteException {
        Log.d(LOG_TAG, "saveDocument() is called");
        Document doc = db.getExistingDocument(id);
        boolean isNewDoc = (doc == null);
        if (isNewDoc) {
            doc = db.createDocument();
        } else {
            properties.put("_rev", doc.getCurrentRevisionId());
        }
        doc.putProperties(properties);
        return doc.getId();
    }

    public LinkedHashMap<String, Object> getById(String id) {
        Document doc = db.getExistingDocument(id);
        if (doc == null) {
            return null;
        }
        LinkedHashMap<String, Object> properties = new LinkedHashMap<>(doc.getProperties());
        properties.remove("_rev");
        properties.put("id", properties.get("_id"));
        properties.remove("_id");
        return properties;
    }

    private Document getDocumentById(String id) {
        Document doc = db.getExistingDocument(id);
        return doc;
    }

    public void registerView(String name, Mapper mapper) {
        View v = db.getView(name);
        v.setMapReduce(mapper, null, "1");
    }

    public <T extends DbObject> List<T> fetchItems(String name, QueryOptions options, Class<T> clazz) {
        Log.d(LOG_TAG, "fetchItems() is called");
        View view = db.getExistingView(name);
        try {
            view.updateIndex();
            QueryIterator<T> iterator = new QueryIterator<>(view.query(options), clazz);
            List<T> rows = new ArrayList<>();
            while (iterator.hasNext()) {
                rows.add(iterator.next());
            }
            return rows;
        } catch (CouchbaseLiteException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            return null;
        }
    }

    public boolean deleteDocument(String docId) {
        try {
            Document document = getDocumentById(docId);
            if (document != null) {
                return document.delete();
            }
            return false;
        } catch (CouchbaseLiteException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            return false;
        }
    }
}
