package edu.ariel.SE_project.worki.signed_in_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;
import edu.ariel.SE_project.worki.data.Shift;
import edu.ariel.SE_project.worki.data.User;
import edu.ariel.SE_project.worki.shift_management.CurrentShifts;
import edu.ariel.SE_project.worki.worker_to_company_registration.MessagesHandler;

public class ListOfUserConnected extends AppCompatActivity
{

    private ListView show_users_online;

    private List<User> users = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_user_connected);


        show_users_online = findViewById(R.id.screenOfUsers);


        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {
                openUsersScreen();
                CurrentShifts.getInstance().addOnShiftsChangedListener(new Consumer<List<Shift>>()
                {
                    @Override
                    public void accept(List<Shift> shifts)
                    {
                        openUsersScreen();
                    }
                });
            }
        });


    }

    private void openUsersScreen()
    {
        Log.d("CurrentShiftUsers", "displaying...");
        List<Shift> shifts = CurrentShifts.getInstance().getShifts();
        List<User> users = null;
        Shift shift = CurrentShifts.getInstance().getCurrentShift();
        User user = CurrentUser.getInstance().getUserData();

        if (shift != null && user != null)// && (user.isManager || shift.getWorkersInShift().contains(user.email)))
        {
            ArrayAdapter<String> userAdapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shift.getWorkersInShift());

            show_users_online.setAdapter(userAdapter);

            Log.d("CurrentShiftUsers", "users: " + shift.getWorkersInShift());
        } else
        {
            Log.d("CurrentShiftUsers", "Faild. Shift: " + shift + ". user: " + user);
        }
    }
}
