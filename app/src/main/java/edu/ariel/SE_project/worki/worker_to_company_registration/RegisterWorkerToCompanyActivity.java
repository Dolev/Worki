package edu.ariel.SE_project.worki.worker_to_company_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;

public class RegisterWorkerToCompanyActivity extends AppCompatActivity
{
    private TextView WorkerMail;
    private Button AddButton;
    private DatabaseReference databaseRef;


//    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_worker_to_company);

        WorkerMail = findViewById(R.id.addWorkerToCompanyTextView);
        AddButton = findViewById(R.id.addWorkerToCompanyButton);


//        databaseUsers = FirebaseDatabase.getInstance().getReference("https://worki-367e9.firebaseio.com/");

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
        if(text == null || text == "" || isUserInDataBase(text))
        {
            return false;
        }
        return true;
    }

    // search for the user's email in the database, if so - saves the message on the firebase.
    private void sendInvitationToWorker(final String mailAddress)
    {
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("email").equalTo(mailAddress);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount() > 0)
                {
                    String id = databaseRef.push().getKey();
                    InviteMessage InviteNewWorker = new InviteMessage(mailAddress, CurrentUser.getInstance().getUserData().email);
                    databaseRef.child(id).setValue(InviteNewWorker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        showSentMessage();
    }

    private boolean isUserInDataBase(String mailAddress)
    {
        if(databaseRef.getRef().child("users").child("userId").child(mailAddress) == null)
        {
            return false;
        }
        return true;

    }

    private void showErrorMessage()
    {
        Toast.makeText(this, "Please Enter a Valid Mail Address.", Toast.LENGTH_SHORT).show();
    }

    private void showSentMessage()
    {
        Toast.makeText(this, "Invitation Was Sent Successfully!", Toast.LENGTH_SHORT).show();
    }
}
