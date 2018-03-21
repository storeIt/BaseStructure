package com.example.om.basestructure.helper;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;

/**
 * Created by android on 05.03.18.
 */

public class NotificationHelper {

    public static Notification buildNotification(Context context, int id, String title, String content, int icon) {
        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500})
                .setSmallIcon(icon);
        if (Build.VERSION.SDK_INT >= 16) {
            return notification.build();
        }
        return notification.getNotification();
    }

}
