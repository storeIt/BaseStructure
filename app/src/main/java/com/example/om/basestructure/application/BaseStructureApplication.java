package com.example.om.basestructure.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.om.basestructure.db.service.DbService;
import com.example.om.basestructure.db.view.ToDoView;

/**
 * Created by android on 11.11.17.
 */

public class BaseStructureApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DbService.getInstance().initDb(getApplicationContext());
        DbService.getInstance().registerView(ToDoView.NAME, new ToDoView());
//        Intent serviceIntent = new Intent(this, DownloadToDoService.class);
//        startService(serviceIntent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
