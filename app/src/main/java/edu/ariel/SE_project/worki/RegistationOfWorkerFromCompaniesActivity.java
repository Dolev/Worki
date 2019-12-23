package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class RegistationOfWorkerFromCompaniesActivity extends AppCompatActivity
{
    public ListView registrationListView;
    Button registrationAcceptButton;
    Button registrationDeclineButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation_of_worker_from_companies);

        registrationListView = findViewById(R.id.ListViewIncomingWorkerInvitations);
        registrationAcceptButton = findViewById(R.id.ButtonAcceptIncomingWorkerInvitations);
        registrationDeclineButton = findViewById(R.id.ButtonDeclineIncomingWorkerInvitations);



        registrationAcceptButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
            }
        });

        registrationDeclineButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
            }
        });
    }
}
