package com.example.om.basestructure.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by android on 24.02.18.
 */

public class ToDoNotificationReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = ToDoNotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = intent.getParcelableExtra("notification");
        int notificationId = intent.getIntExtra("notificationId", 0);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification);
    }

}
