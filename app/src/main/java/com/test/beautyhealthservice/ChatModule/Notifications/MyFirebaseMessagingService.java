package com.test.beautyhealthservice.ChatModule.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.test.beautyhealthservice.ChatModule.MessageActivity;
import com.test.beautyhealthservice.MainActivity;
import com.test.beautyhealthservice.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManager notifManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("user"));
        } else {
            createNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("user"));
        }

    }
    public void createNotification(String aMessage,String user_id) {
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
                mChannel.setLightColor(Color.GREEN);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            Intent intent_activity = new Intent(this, MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("receiver_id", user_id);
            intent_activity.putExtras(bundle);
            intent_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent_activity = PendingIntent.getActivity(this, 0, intent_activity, PendingIntent.FLAG_MUTABLE);

            builder.setContentTitle("Beauty Health Service")
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(this.getString(R.string.app_name))
                    .setDefaults(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentText(aMessage)
                    .setContentIntent(pendingIntent_activity)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);

            Intent intent_activity = new Intent(this, MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userid", user_id);
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
