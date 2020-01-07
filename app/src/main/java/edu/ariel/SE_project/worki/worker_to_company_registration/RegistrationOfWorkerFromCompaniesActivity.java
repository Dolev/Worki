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

//    private ArrayList<InviteMessage> myMess;
    private ArrayAdapter arrAdap;
    private InviteMessage itemChosen;

    private DatabaseReference databaseMessagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation_of_worker_from_companies);


        databaseMessagesRef = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath);

        registrationListView = findViewById(R.id.ListViewIncomingWorkerInvitations);
        registrationAcceptButton = findViewById(R.id.ButtonAcceptIncomingWorkerInvitations);
        registrationDeclineButton = findViewById(R.id.ButtonDeclineIncomingWorkerInvitations);

//        myMess = new ArrayList<>();

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
                if (MessagesHandler.inviteWorkers.isEmpty() || MessagesHandler.inviteWorkers.get(CurrentUser.getInstance().getUserData().id).isEmpty())
                {
                    setButtonsClickable(true);
                    itemChosen = (InviteMessage)parent.getSelectedItem();          // for later use of accept/decline
                }
                else
                {
                    setButtonsClickable(false);
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
    // done
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
                    MessagesHandler.insertByKey(MessagesHandler.inviteWorkers, CurrentUser.getInstance().getUserData().id, mess);
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
    // done
    private void updateMessagesListView()
    {
        arrAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                                   MessagesHandler.inviteWorkers.get(CurrentUser.getInstance().getUserData().id));
        registrationListView.setAdapter(arrAdap);
    }

    // deletes declined message from listview
    // half done
    private void messageDeclined(InviteMessage itemChosen)
    {
        String id = databaseMessagesRef.push().getKey();

        InviteMessage inviteMessageDeclined = new InviteMessage(itemChosen);
        inviteMessageDeclined.setCurrentStatus(InviteMessage.invitationStatus.declined);
        inviteMessageDeclined.writeToDatabase(databaseMessagesRef);
//        databaseRef.child(id).setValue(inviteMessageDeclined);

        MessagesHandler.sendReplyToManager(inviteMessageDeclined);

        MessagesHandler.inviteWorkers.remove(itemChosen);
        updateMessagesListView();
    }

    // changes message status to accepted, deletes all messages from listview
    // half done
    private void messageAccepted(InviteMessage itemChosen)
    {
        String id = databaseMessagesRef.push().getKey();

        InviteMessage inviteMessageAccepted = new InviteMessage(itemChosen);
        inviteMessageAccepted.setCurrentStatus(InviteMessage.invitationStatus.accepted);
//        databaseRef.child(id).setValue(inviteMessageAccepted);
        inviteMessageAccepted.writeToDatabase(databaseMessagesRef);

        MessagesHandler.sendReplyToManager(inviteMessageAccepted);

        deleteAllInviteMessages();
        updateMessagesListView();
        Toast.makeText(this, "Invitation has been accepted Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void deleteAllInviteMessages()
    {
        MessagesHandler.inviteWorkers.get(CurrentUser.getInstance().getUserData().id).clear();
    }

    private void noMessagesToShow()
    {
        Toast.makeText(this, "There are no Messages to show", Toast.LENGTH_SHORT).show();
    }

}
