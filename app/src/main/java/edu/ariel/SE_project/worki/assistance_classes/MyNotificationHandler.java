package edu.ariel.SE_project.worki.assistance_classes;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import androidx.core.app.NotificationCompat;

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
}
