package edu.ariel.SE_project.worki.worker_to_company_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Bundle;
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

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.assistance_classes.GlobalMetaData;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;
import edu.ariel.SE_project.worki.data.User;

public class RegisterWorkerToCompanyActivity extends AppCompatActivity
{
    private TextView WorkerMail;
    private Button AddButton;
    private Button ClearButton;
    private ListView repliesListview;

    private ArrayList<String> myReplies;
    private ArrayAdapter arrAdap;

    private DatabaseReference databaseUsersRef;
    private DatabaseReference databaseMessagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_worker_to_company);

        WorkerMail = findViewById(R.id.addWorkerToCompanyTextView);
        AddButton = findViewById(R.id.addWorkerToCompanyButton);

        ClearButton = findViewById(R.id.clearRecievedRepliesFromWorkersButton);
        repliesListview = findViewById(R.id.managerRepliesFromWorkersListView);

//        myReplies = new ArrayList<>();

        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {
                searchForNewReplies();
//                updateRepliesListView();

            }
        });


        // shows replies recieved from workers
        repliesListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (!MessagesHandler.workerReplies.containsKey(CurrentUser.getInstance().getUserData().id) ||
                        MessagesHandler.workerReplies.get(CurrentUser.getInstance().getUserData().id).isEmpty())
                {
                    ClearButton.setEnabled(false);
                } else
                {
                    ClearButton.setEnabled(true);
                }

            }
        });

        AddButton.setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v)
                                         {
                                             if (mailIsValid(WorkerMail.getText().toString()))
                                             {
                                                 sendInvitationToWorker(WorkerMail.getText().toString());
                                             } else
                                             {
                                                 showErrorMessage();
                                             }
                                         }
                                     }
        );

        //done
        ClearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MessagesHandler.deleteMessage(false, CurrentUser.getInstance().getUserData().id);
            }
        });
    }

    // done
    private void updateRepliesListView()
    {
        if (MessagesHandler.workerReplies.containsKey(CurrentUser.getInstance().getUserData().id))
        {
            myReplies = new ArrayList<>();
            myReplies = MessagesHandler.convertToStrings(MessagesHandler.workerReplies.get(CurrentUser.getInstance().getUserData().id));

            arrAdap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myReplies);
            repliesListview.setAdapter(arrAdap);
        }
    }

    private void searchForNewReplies()
    {
        // todo implement send AnswerToManager, need some kind of static object to handle messages
        if (MessagesHandler.workerReplies.containsKey(CurrentUser.getInstance().getUserData().id))
        {
            updateRepliesListView();
        }

    }

    private boolean mailIsValid(String text)
    {
        if (text == null || text.isEmpty() || isUserInDataBase(text))
        {
            return false;
        }
        return true;
    }

    // search for the user's email in the database, if so - saves the message on the firebase.
    private void sendInvitationToWorker(final String mailAddress)
    {
        databaseMessagesRef = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath);
        databaseUsersRef = FirebaseDatabase.getInstance().getReference(GlobalMetaData.usersPath);
        Query query = FirebaseDatabase.getInstance().getReference(GlobalMetaData.usersPath).orderByChild("email").equalTo(mailAddress);
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getChildrenCount() > 0)
                {
//                    String id = databaseRef.push().getKey();
                    InviteMessage InviteNewWorker = new InviteMessage();
                    InviteNewWorker.readFromDatabase(dataSnapshot);
                    InviteNewWorker.writeToDatabase(databaseMessagesRef.push());
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
        if (databaseUsersRef.getRef().child("users").child("userId").child(mailAddress) == null)
        {
            return false;
        }
        return true;

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
