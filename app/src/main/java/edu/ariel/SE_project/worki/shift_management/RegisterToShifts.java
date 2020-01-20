package edu.ariel.SE_project.worki.shift_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Consumer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.se.omapi.Channel;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.Shift;
import edu.ariel.SE_project.worki.data.User;
import edu.ariel.SE_project.worki.signed_in_activities.TimerActivity;

public class RegisterToShifts extends AppCompatActivity implements View.OnClickListener
{
    private ListView shiftsView;
    private Button accept, reject;
    private List<Shift> shifts = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_shifts);

        shiftsView = findViewById(R.id.shift_list);
        shiftsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        accept = findViewById(R.id.add);
        reject = findViewById(R.id.remove);

        CurrentShifts.getInstance().addOnShiftsChangedListener(new Consumer<List<Shift>>()
        {
            @Override
            public void accept(List<Shift> shifts)
            {
                updateUI(shifts);
            }
        });

        accept.setOnClickListener(this);
        reject.setOnClickListener(this);
    }

    private void updateUI(List<Shift> shifts)
    {
        shiftsView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shifts));
        this.shifts = shifts;
    }

    @Override
    public void onClick(View v)
    {
        if (shifts == null)
            return;
        Shift selection = shifts.get(shiftsView.getCheckedItemPosition());
        User user = CurrentUser.getInstance().getUserData();
        if (v.equals(accept))
        {
            if (!selection.getWorkersInShift().contains(user))
                CurrentShifts.getInstance().registerToShift(selection, user);
            //add notification
            addNotification();

        } else if (v.equals(reject))
        {
            if (selection.getWorkersInShift().contains(user))
                CurrentShifts.getInstance().unregisterToShift(selection, user);
            //dellete notification


        }

    }

    private void createNotificationChannel(String CHANNEL_ID)
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


    public void addNotification()
    {
        String CHANNEL_ID = NotificationChannel.DEFAULT_CHANNEL_ID;
        createNotificationChannel(CHANNEL_ID);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Worki- Shift is about to Start")
                .setContentText("Please dont be late!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Please dont be late!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_CALL);


    }
}
