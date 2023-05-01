package com.test.beautyhealthservice.ChatModule.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.test.beautyhealthservice.ChatModule.MessageActivity;
import com.test.beautyhealthservice.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManager notifManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("sender_id"),remoteMessage.getData().get("sender_name"),remoteMessage.getData().get("image_url"));
        } else {
            createNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("sender_id"),remoteMessage.getData().get("sender_name"),remoteMessage.getData().get("image_url"));
        }

    }
    public void createNotification(String aMessage,String sender_id,String sender_name,String image_url) {
        final int NOTIFY_ID = 1002;

        String name = "example_name";
        String id = "example_id";
        String description = "example_description";

        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            Intent intent_activity = new Intent(this, MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("receiver_id", sender_id);
            bundle.putString("name", sender_name);

            System.out.println("Name"+image_url);
            bundle.putString("image", image_url);

            intent_activity.putExtras(bundle);
            intent_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent_activity = PendingIntent.getActivity(this, 0, intent_activity, PendingIntent.FLAG_MUTABLE);

            builder.setContentTitle("Beauty Health Service")
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(this.getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentText(aMessage)
                    .setContentIntent(pendingIntent_activity);
        } else {

            builder = new NotificationCompat.Builder(this);

            Intent intent_activity = new Intent(this, MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("receiver_id", sender_id);
            bundle.putString("name", sender_name);
            System.out.println("Name"+image_url);
            bundle.putString("image", image_url);
            intent_activity.putExtras(bundle);
            intent_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent_activity = PendingIntent.getActivity(this, 0, intent_activity, PendingIntent.FLAG_ONE_SHOT);

            builder.setContentTitle(this.getString(R.string.app_name))
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(this.getString(R.string.app_name))
                    .setDefaults(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentText(aMessage)
                    .setContentIntent(pendingIntent_activity)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);


    }
}
