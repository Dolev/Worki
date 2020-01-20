package edu.ariel.SE_project.worki.worker_to_company_registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

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

    private ArrayAdapter arrAdap;
    private InviteMessage itemChosen;

    private List<InviteMessage> items;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation_of_worker_from_companies);

        registrationListView = findViewById(R.id.ListViewIncomingWorkerInvitations);
        registrationAcceptButton = findViewById(R.id.ButtonAcceptIncomingWorkerInvitations);
        registrationDeclineButton = findViewById(R.id.ButtonDeclineIncomingWorkerInvitations);

        registrationListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {
                MessagesHandler.getInstance().addOnMessagesChangedListener(new Consumer<List<InviteMessage>>()
                {
                    @Override
                    public void accept(List<InviteMessage> inviteMessages)
                    {
                        updateMessagesListView();
                    }
                });
            }
        });


        registrationListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (MessagesHandler.getInstance().getMessages().isEmpty())
                {
                    setButtonsClickable(false);
                } else
                {
                    setButtonsClickable(true);
                    itemChosen = items.get(position);          // for later use of accept/decline
                    Log.d("Registration-Worker", itemChosen + " clicked");
                }

            }
        });


        registrationAcceptButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                if (itemChosen != null && CurrentUser.getInstance().getUserData() != null)
                {
                    acceptMessage(itemChosen);
                } else
                {
                    Log.w("Registration-Worker", "Accept Button: user or itemChosen is Null");
                }
            }
        });

        registrationDeclineButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                if (itemChosen != null && CurrentUser.getInstance().getUserData() != null)
                {
                    declineMessage(itemChosen);
                } else
                {
                    Log.w("Registration-Worker", "Decline Button: user or itemChosen is Null");
                }
            }
        });
    }

    // turning the buttons appearance on/off
    private void setButtonsClickable(boolean b)
    {
        registrationAcceptButton.setEnabled(b);
        registrationDeclineButton.setEnabled(b);
    }


    // search for invite messages sent from managers to this user's email, saves them on a list.
//    public void searchForNewMessages(User user)
//    {
//        Query query = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath).orderByChild("email")
//                .equalTo(user.email);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                for (DataSnapshot ds : dataSnapshot.getChildren())
//                {
//                    InviteMessage mess = ds.getValue(InviteMessage.class);
//                    MessagesHandler.sendMessage(false, mess);
//                    updateMessagesListView();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//                noMessagesToShow();
//            }
//        });
//    }


    // updates the listview ui
    private void updateMessagesListView()
    {
        List<InviteMessage> messages = new LinkedList<>();

        for (InviteMessage mess : MessagesHandler.getInstance().getMessages())
        {
            if (mess.getCurrentStatus() != InviteMessage.InvitationStatus.declined)
                messages.add(mess);
        }


        items = messages;

//        if (MessagesHandler.getInstance().getMessages().contains(CurrentUser.getInstance().getUserData().email))
        arrAdap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                MessagesHandler.convertToStrings(messages));                          // how to get specific user's messages?

        registrationListView.setAdapter(arrAdap);
    }

    private void acceptMessage(InviteMessage mess)
    {
        InviteMessage inviteMessageAccepted = new InviteMessage(mess);
        inviteMessageAccepted.setCurrentStatus(InviteMessage.InvitationStatus.accepted);
        MessagesHandler.getInstance().updateMessage(inviteMessageAccepted);

        MessagesHandler.getInstance().updateMessage(inviteMessageAccepted.replyMessage());

        declineAll(mess);

        User user = CurrentUser.getInstance().getUserData();
        user.companyId = mess.getCompanyId();

        CurrentUser.getInstance().updateUserData(user);

        updateMessagesListView();
        Toast.makeText(this, "Invitation has been accepted Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void declineMessage(InviteMessage mess)
    {
        InviteMessage inviteMessageDeclined = new InviteMessage(mess);
        inviteMessageDeclined.setCurrentStatus(InviteMessage.InvitationStatus.declined);
        MessagesHandler.getInstance().updateMessage(inviteMessageDeclined);

        MessagesHandler.getInstance().sendMessage(inviteMessageDeclined.replyMessage());
    }

    private void declineAll(InviteMessage exception)
    {
        for (InviteMessage mess : MessagesHandler.getInstance().getMessages())
        {
            if (!mess.equals(exception) && mess.getCurrentStatus() == InviteMessage.InvitationStatus.undecided)
                declineMessage(mess);
        }
    }

    private void noMessagesToShow()
    {
        Toast.makeText(this, "There are no Messages to show", Toast.LENGTH_SHORT).show();
    }

}
