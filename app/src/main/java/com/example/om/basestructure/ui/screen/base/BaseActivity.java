package com.example.om.basestructure.ui.screen.base;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.om.basestructure.R;
import com.example.om.basestructure.helper.ContextProvider;
import com.example.om.basestructure.ui.fragment.ServerFragment;
import com.example.om.basestructure.ui.fragment.ToDoDetailsFragment;
import com.example.om.basestructure.ui.fragment.ToDoListFragment;
import com.example.om.basestructure.ui.fragment.base.BaseDialog;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;
import com.example.om.basestructure.ui.fragment.dialog.DialogCalendar;
import com.example.om.basestructure.ui.fragment.dialog.DialogTime;
import com.example.om.basestructure.ui.screen.MainActivity;

/**
 * Created by om on 10/17/17.
 */

public abstract class
BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    public boolean hasEntityChanged = false;
    private BaseDialog lastUsedDialog = null;

    public void replaceFragment(int fragmentContainer, String fragmentTag, Object data, Bundle arguments) {
        BaseFragment f = (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (f == null) {
            f = initFragment(fragmentTag);
        }
        if (data != null) {
            f.setData(data);
        }
        if (arguments != null) {
            f.setArguments(arguments);
        }
        getSupportFragmentManager().beginTransaction().replace(fragmentContainer, f)
                .addToBackStack(null)
                .commit();
    }

    private BaseFragment initFragment(String fragmentTag) {
        if (ToDoListFragment.FRAGMENT_TAG.equals(fragmentTag)) {
            return new ToDoListFragment();
        } else if (ToDoDetailsFragment.FRAGMENT_TAG.equals(fragmentTag)) {
            return new ToDoDetailsFragment();
        } else if (DialogCalendar.FRAGMENT_TAG.equals(fragmentTag)) {
            //  return new DialogCalendar();
        } else if (ServerFragment.FRAGMENT_TAG.equals(fragmentTag)) {
            return new ServerFragment();
        }
        throw new IllegalArgumentException("Unhandled fragmentTag");
    }

    @Override
    protected void onResume() {
        ContextProvider.getInstance().setContext(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        ContextProvider.getInstance().setContext(null);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            showAppDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        finish();
                    } else {
                        dialog.dismiss();
                    }
                }
            }, R.string.leave_app_dialog_msg, R.string.yes, R.string.no);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public <T extends BroadcastReceiver> void scheduleNotification(int id, String title,
             String content, int icon, long when, String entityId, String fragmentTag, Class<T> receiverClass) {
        Notification notification = buildNotification(id, title, content, icon, entityId, fragmentTag);
        Intent notificationIntent = new Intent(this, receiverClass);
        notificationIntent.putExtra("notification", notification);
        notificationIntent.putExtra("notificationId", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
    }

    private Notification buildNotification(int id, String title, String content, int icon, String entityId, String fragmentTag) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fragmentTag", fragmentTag);
        bundle.putString("id", entityId);
        intent.putExtras(bundle);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500})
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= 16) {
            return notification.build();
        }
        return notification.getNotification();
    }

    public <T extends BroadcastReceiver> void stopNotificationAlarm(int id, Class<T> receiverClass) {
        Intent notificationIntent = new Intent(this, receiverClass);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    public void showDialog(String fragmentTag, BaseFragment targetFragment, boolean closeCurrent, Object... data) {
        if (closeCurrent && lastUsedDialog != null) {
            lastUsedDialog.dismiss();
        }
        if (fragmentTag.equals(DialogCalendar.FRAGMENT_TAG)) {
            BaseDialog dialog = new DialogCalendar();

            dialog.setData(data);
            dialog.setTargetFragment(targetFragment, 0);
            lastUsedDialog = dialog;
            dialog.show(getSupportFragmentManager(), fragmentTag);
        } else if (fragmentTag.equals(DialogTime.FRAGMENT_TAG)) {
            BaseDialog dialog = new DialogTime();
            dialog.setData(data);
            dialog.setTargetFragment(targetFragment, 0);
            lastUsedDialog = dialog;
            dialog.show(getSupportFragmentManager(), fragmentTag);
        }
    }

    public void showAppDialog(DialogInterface.OnClickListener onClickListener,
                              int message, int positiveText, int negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setNegativeButton(negativeText, onClickListener)
                .setPositiveButton(positiveText, onClickListener);
        builder.create().show();
    }
}
