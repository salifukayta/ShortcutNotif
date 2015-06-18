package com.shortcut.sol.shortcutnotif.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.shortcut.sol.shortcutnotif.ConstEnum;

/**
 * Created by safeki on 09/06/2015.
 */
public class NotificationListener extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    NotificationServiceReceiver notificationServiceReciver;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationServiceReciver = new NotificationServiceReceiver();
        IntentFilter intentFilter = new IntentFilter(ConstEnum.GET_NOTIFICATION.getValue());
        registerReceiver(notificationServiceReciver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationServiceReciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "**********  onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "********** onNOtificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        Log.i(TAG, "********** getActiveNotifications.length=" + super.getActiveNotifications().length);
        return super.getActiveNotifications();
    }

    class NotificationServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int idNotification = intent.getIntExtra(ConstEnum.NOTIFICATION_ID.getValue(), -1);
            Bundle extras = intent.getExtras();
            Log.i(TAG, "******* onReceive" + idNotification);
            for (String key: extras.keySet()) {
                Log.i(TAG, "******* key=" + key);
            }
            for (StatusBarNotification sbn : NotificationListener.this.getActiveNotifications()) {
                if (idNotification == sbn.getId()) {
                    Intent i = new Intent(ConstEnum.GET_NOTIFICATION_RESPONSE.getValue());
                    i.putExtra(ConstEnum.NOTIFICATION_ID_EXIST.getValue(), true);
                    sendBroadcast(i);
                    return;
                }
            }
            Intent i = new Intent(ConstEnum.GET_NOTIFICATION_RESPONSE.getValue());
            i.putExtra(ConstEnum.NOTIFICATION_ID_EXIST.getValue(), false);
            sendBroadcast(i);
        }
    }
}
