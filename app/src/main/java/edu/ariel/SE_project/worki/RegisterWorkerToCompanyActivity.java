package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ariel.SE_project.worki.data.InviteMessageObj;

public class RegisterWorkerToCompanyActivity extends AppCompatActivity
{
    TextView WorkerMail;
    Button AddButton;
    DatabaseReference databaseMessages;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_worker_to_company);

        WorkerMail = findViewById(R.id.addWorkerToCompanyTextView);
        AddButton = findViewById(R.id.addWorkerToCompanyButton);

        databaseMessages = FirebaseDatabase.getInstance().getReference("messages");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        AddButton.setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v)
                                         {
                                                  if(mailIsValid(WorkerMail.getText().toString()))
                                                  {
                                                         sendInvitationToWorker(WorkerMail.getText().toString());
                                                  }
                                                  else
                                                  {
                                                         showErrorMessage();
                                                  }
                                         }
                                     }
        );
    }

    private boolean mailIsValid(String text)
    {
        if(text == null)
        {
            return false;
        }

        // TODO search if the mail entered exists on the database of users
//        if(text)              // if mail doesnt exist in database
//        {
//            return false;
//        }

        return true;
    }

    private void showErrorMessage()
    {
        Toast.makeText(this, "Please Enter a Valid Mail Address.", Toast.LENGTH_SHORT).show();
    }

    private void sendInvitationToWorker(String mailAddress)
    {
        String id = databaseMessages.push().getKey();
        InviteMessageObj InviteNewWorker = new InviteMessageObj(mailAddress,"manager", id);
        databaseMessages.child(id).setValue(InviteNewWorker);

        showSentMessage();
    }

    private void showSentMessage()
    {
        Toast.makeText(this, "Invitation Was Sent Successfully!", Toast.LENGTH_SHORT).show();
    }
}
