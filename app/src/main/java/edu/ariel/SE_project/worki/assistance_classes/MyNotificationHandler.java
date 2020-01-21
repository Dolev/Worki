package edu.ariel.SE_project.worki.assistance_classes;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.ariel.SE_project.worki.MainActivity;
import edu.ariel.SE_project.worki.R;

public class MyNotificationHandler
{
    public static void sendNotification(Activity activity, String title, String text, int id)
    {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(text);


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//
        if (mNotificationManager != null)
            mNotificationManager.notify(id, mBuilder.build());
    }

    private static String CHANNEL_ID;

    private static void createNotificationChannel(Activity activity)
    {
        CharSequence channelName = CHANNEL_ID;
        String channelDesc = "channelDesc";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
    }


    public static void createNotification(Activity activity, String title, String text)
    {

        CHANNEL_ID = R.string.app_name + "";
        if (text != null)
        {
            createNotificationChannel(activity);

            Intent intent = Transitions.getNotificationIntent(activity);

            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
            int notificationId = (int) (System.currentTimeMillis() / 4);
            notificationManager.notify(notificationId, mBuilder.build());

            Log.d("NotificationHandler", "Notification sent. Title: " + title + ", Text: " + text);
        }
    }
}
