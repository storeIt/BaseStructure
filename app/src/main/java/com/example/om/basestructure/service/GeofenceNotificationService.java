package com.example.om.basestructure.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by android on 05.03.18.
 */

public class GeofenceNotificationService extends IntentService {

    public GeofenceNotificationService() {
        super("GeofenceNotificationService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public GeofenceNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent != null && !geofencingEvent.hasError()) {
            Notification notification = intent.getParcelableExtra("notification");
            int notificationId = intent.getIntExtra("notificationId", 0);
            NotificationManager mNotificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationId, notification);
        }
    }
}
