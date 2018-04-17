package com.example.om.basestructure.db.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by android on 25.11.17.
 */

public class DbObject extends JSONObject {

    private String id;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        Log.i("LOG", "setId() is called");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
