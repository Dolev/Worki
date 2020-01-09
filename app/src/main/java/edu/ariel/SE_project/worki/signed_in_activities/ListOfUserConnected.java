package edu.ariel.SE_project.worki.signed_in_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private Button showCurrentUsers;
    private ListView show_users_online;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_user_connected);

        showCurrentUsers = findViewById(R.id.showCurrentUsers);
        show_users_online = findViewById(R.id.screenOfUsers);


        showCurrentUsers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                if (showCurrentUsers.isClickable() && CurrentUser.getInstance().getUserData() != null)
                {
                    openUsersScreen();
                }
            }
        });


    }

    private void openUsersScreen()
    {
        List<Shift> shifts = CurrentShifts.getInstance().getShifts();
        List<User> users = null;
        for (Shift s : shifts)
        {
            Date current = new Date();
            if (s.getShiftDate().before(current) && s.getShiftEnd().after(current))
                if ((s.getWorkersInShift().contains(CurrentUser.getInstance().getUserData())))
                {                //insert workers
                    ArrayAdapter<User> userAdapter =
                            new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1,s.getWorkersInShift());

                    show_users_online.setAdapter(userAdapter);
                }
        }



    }
}
