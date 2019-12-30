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

public class RegistrationOfWorkerFromCompaniesActivity extends AppCompatActivity
{
    private ListView registrationListView;
    private Button registrationAcceptButton;
    private Button registrationDeclineButton;

    private DatabaseReference myRef;
    private ArrayList<InviteMessage> myMess;
    private ArrayAdapter arrAdap;
    private int itemChosen;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation_of_worker_from_companies);


        myRef = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath);

        registrationListView = findViewById(R.id.ListViewIncomingWorkerInvitations);
        registrationAcceptButton = findViewById(R.id.ButtonAcceptIncomingWorkerInvitations);
        registrationDeclineButton = findViewById(R.id.ButtonDeclineIncomingWorkerInvitations);

        myMess = new ArrayList<>();

        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {

                searchForNewMessages(user);
                updateMessagesListView();
            }
        });


        registrationListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (myMess.isEmpty())
                {
                    setButtonsClickable(false);
                }
                else
                {
                    setButtonsClickable(true);
                    itemChosen = parent.getSelectedItemPosition();          // for later use of accept/decline
                }

            }
        });


        registrationAcceptButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                if(registrationAcceptButton.isClickable())
                {
                    messageAccepted(itemChosen);
                }
            }
        });

        registrationDeclineButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                if(registrationDeclineButton.isClickable())
                {
                    messageDeclined(itemChosen);
                }

            }
        });
    }

    // turning the buttons apearance on/off
    private void setButtonsClickable(boolean b)
    {
        registrationAcceptButton.setEnabled(b);
    }


    // search for invite messages sent from managers to this user's email, saves them on a list.
    public void searchForNewMessages(User user)
    {
        Query query = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath).orderByChild("email")
                .equalTo(user.email);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    InviteMessage mess = ds.getValue(InviteMessage.class);
                    myMess.add(mess);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                noMessagesToShow();
            }
        });
    }


    // updates the listview ui
    private void updateMessagesListView()
    {
        arrAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myMess);
        registrationListView.setAdapter(arrAdap);
    }

    // deletes declined message from listview
    private void messageDeclined(int itemChosen)
    {
        String id = myRef.push().getKey();
        InviteMessage inviteMessageDeclined = myMess.get(itemChosen);
        inviteMessageDeclined.setCurrentStatus(InviteMessage.invitationStatus.declined);
        myRef.child(id).setValue(inviteMessageDeclined);

        // todo implement send AnswerToManager
        sendAnswerToManager(inviteMessageDeclined);

        myMess.remove(itemChosen);
        updateMessagesListView();
    }

    // changes message status to accepted, deletes all messages from listview
    private void messageAccepted(int itemChosen)
    {
        String id = myRef.push().getKey();
        InviteMessage inviteMessageAccepted = myMess.get(itemChosen);
        inviteMessageAccepted.setCurrentStatus(InviteMessage.invitationStatus.accepted);
        myRef.child(id).setValue(inviteMessageAccepted);

        // todo implement send AnswerToManager
        sendAnswerToManager(inviteMessageAccepted);

        deleteAllInviteMessages();
        updateMessagesListView();
        Toast.makeText(this, "Invitation has been accepted Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void deleteAllInviteMessages()
    {
        myMess.clear();
    }

    private void noMessagesToShow()
    {
        Toast.makeText(this, "There are no Messages to show", Toast.LENGTH_SHORT).show();
    }

    private void sendAnswerToManager(InviteMessage inviteMessage)
    {
        InviteMessage replyToManager = new InviteMessage(inviteMessage);
        replyToManager.setReply(true);
        replyToManager.inverseRecipientSender();
        // todo send the reply
    }
}
