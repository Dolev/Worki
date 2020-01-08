package edu.ariel.SE_project.worki.signed_in_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.Shift;

public class ListOfUserConnected extends AppCompatActivity
{
    private Button showCurrentUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_user_connected);

        showCurrentUsers= findViewById(R.id.ShowCurrentUsers);


            if (Shift.workersInShift!=null)
            {

            }


    }
}
