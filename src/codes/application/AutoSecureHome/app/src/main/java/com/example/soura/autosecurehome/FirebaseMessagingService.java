package com.example.soura.autosecurehome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notification_title =remoteMessage.getNotification().getTitle();
        String notification_message =remoteMessage.getNotification().getBody();

        Notification.Builder builder =new Notification.Builder(this);

        long arr[]={500,1000,500,1000,500,1000};

        Notification notification = builder
                .setContentTitle(notification_title)
                .setContentText(notification_message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(arr)
                .build();

        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        notification.defaults = Notification.DEFAULT_LIGHTS ;

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, builder.build());
    }
}