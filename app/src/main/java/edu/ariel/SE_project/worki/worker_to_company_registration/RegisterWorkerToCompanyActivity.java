package edu.ariel.SE_project.worki.worker_to_company_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.assistance_classes.GlobalMetaData;
import edu.ariel.SE_project.worki.data.Company;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;
import edu.ariel.SE_project.worki.data.User;

// this appear on manager page
public class RegisterWorkerToCompanyActivity extends AppCompatActivity
{
    private TextView WorkerMail;
    private Button AddButton;
    private Button ClearButton;
    private ListView repliesListview;

    private List<String> myReplies;
    private ArrayAdapter arrAdap;
    private DatabaseReference databaseUsersRef;
    private DatabaseReference databaseMessagesRef;

    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MessagesHandler.setContext(this);
        setContentView(R.layout.activity_register_worker_to_company);

        WorkerMail = findViewById(R.id.addWorkerToCompanyTextView);
        AddButton = findViewById(R.id.addWorkerToCompanyButton);

        ClearButton = findViewById(R.id.clearRecievedRepliesFromWorkersButton);
        repliesListview = findViewById(R.id.managerRepliesFromWorkersListView);


        // shows replies received from workers
        repliesListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (MessagesHandler.getInstance().getMessages().isEmpty())
                {
                    ClearButton.setEnabled(false);
                } else
                {
                    ClearButton.setEnabled(true);
                }

            }
        });

        MessagesHandler.getInstance().addOnMessagesChangedListener(new Consumer<List<InviteMessage>>()
        {
            @Override
            public void accept(List<InviteMessage> inviteMessages)
            {
                updateRepliesListView();
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v)
                                         {
                                             mailIsValid(WorkerMail.getText().toString());
                                         }
                                     }
        );

        //done
        ClearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideAll();
            }
        });
    }

    private void hideAll()
    {
        for (InviteMessage mess :
                MessagesHandler.getInstance().getMessages())
        {
            if (mess.isShow())
                MessagesHandler.getInstance().updateMessage(mess.hidden());
        }
    }

    private void addToCompany(List<InviteMessage> allMessages)
    {
        Company company = CurrentUser.getInstance().getCompany();

        if (company == null)
            return;

        boolean updated = false;

        for (InviteMessage mess : allMessages)
        {
            if (mess.getCurrentStatus() == InviteMessage.InvitationStatus.accepted &&
                    !company.workers.contains(mess.getSender()))
            {
                company.workers.add(mess.getSender());
                updated = true;
            }
        }
        if (updated)
            CurrentUser.getInstance().updateCompanyData(company);
    }

    // done
    private void updateRepliesListView()
    {
        myReplies = new ArrayList<>();
        myReplies = MessagesHandler.convertToStrings(MessagesHandler.getInstance().getMessages());

        arrAdap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myReplies);
        repliesListview.setAdapter(arrAdap);
    }

    // searches for this mail address on users db
    private void mailIsValid(final String text)
    {

        Query query = FirebaseDatabase.getInstance().getReference(GlobalMetaData.usersPath).orderByChild("email").equalTo(text);
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                sendInvitationToWorker(WorkerMail.getText().toString(), dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                showErrorMessage();
            }

        });
    }

    // search for the user's email in the database, if so - saves the message on the firebase.
    private void sendInvitationToWorker(final String mailAddress, DataSnapshot snapshot)
    {

        Log.d("ds: ", snapshot.toString());
        InviteMessage inviteNewWorker = new InviteMessage(mailAddress, CurrentUser.getInstance().getUserData().email,
                InviteMessage.InvitationStatus.undecided, true, CurrentUser.getInstance().getUserData().companyId);

        MessagesHandler.getInstance().sendMessage(inviteNewWorker);
        showSentMessage();
    }


    // done
    private void showErrorMessage()
    {
        Toast.makeText(this, "Please Enter a Valid Mail Address.", Toast.LENGTH_SHORT).show();
    }

    //done
    private void showSentMessage()
    {
        Toast.makeText(this, "Invitation Was Sent Successfully!", Toast.LENGTH_SHORT).show();
    }
}
