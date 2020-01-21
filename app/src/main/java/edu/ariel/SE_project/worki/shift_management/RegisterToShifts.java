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
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }

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

        accept.setEnabled(false);
        reject.setEnabled(false);

        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {

                accept.setEnabled(true);
                reject.setEnabled(true);
            }
        });

        accept.setOnClickListener(this);;   {}

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
        final Shift selection = shifts.get(shiftsView.getCheckedItemPosition());
        User user = CurrentUser.getInstance().getUserData();
        if (v.equals(accept))
        {

            if (!selection.getWorkersInShift().contains(user.email))
                CurrentShifts.getInstance().registerToShift(selection, user);
        } else if (v.equals(reject))
        {
            if (selection.getWorkersInShift().contains(user.email))
                CurrentShifts.getInstance().unregisterToShift(selection, user);
        }

    }
}
