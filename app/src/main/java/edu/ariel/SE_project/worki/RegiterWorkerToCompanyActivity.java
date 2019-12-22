package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegiterWorkerToCompanyActivity extends AppCompatActivity
{
    TextView WorkerMail;
    Button AddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter_worker_to_company);

        WorkerMail = findViewById(R.id.addWorkerToCompanyTextView);
        AddButton = findViewById(R.id.addWorkerToCompanyButton);

        AddButton.setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v)
                                         {
                                                  if(WorkerMail.getText() != null)
                                                  {
                                                         sendInvitationToWorker(WorkerMail.getText());
                                                  }
                                                  else
                                                  {
                                                         showErrorMessage();
                                                  }
                                         }
                                     }
        );
    }

    private void showErrorMessage()
    {
        Toast.makeText(this, "Please Enter a Valid Address.", Toast.LENGTH_SHORT).show();
    }

    private void sendInvitationToWorker(CharSequence mailAdress)
    {
            // TODO send invitation to worker
    }
}
