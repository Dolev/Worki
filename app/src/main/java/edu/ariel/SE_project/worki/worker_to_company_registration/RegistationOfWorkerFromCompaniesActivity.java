package edu.ariel.SE_project.worki.worker_to_company_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;

public class RegistationOfWorkerFromCompaniesActivity extends AppCompatActivity
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

        registrationListView = findViewById(R.id.ListViewIncomingWorkerInvitations);
        registrationAcceptButton = findViewById(R.id.ButtonAcceptIncomingWorkerInvitations);
        registrationDeclineButton = findViewById(R.id.ButtonDeclineIncomingWorkerInvitations);

        myMess = new ArrayList<>();

        searchForNewMessages();
        updateMessagesListView();



        registrationListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                itemChosen = parent.getSelectedItemPosition();          // for later use of accept/decline
            }
        });



        registrationAcceptButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                messageAccepted(itemChosen);

            }
        });

        registrationDeclineButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
                messageDeclined(itemChosen);
            }
        });
    }



    // search for invite messages sent from managers to this user's email, saves them on a list.
    public void searchForNewMessages()
    {
        myRef = FirebaseDatabase.getInstance().getReference("messages");
        Query query = FirebaseDatabase.getInstance().getReference("messages").orderByChild("email")
                .equalTo(CurrentUser.getInstance().getUserData().email);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    InviteMessage mess = ds.getValue(InviteMessage.class);
                    myMess.add(mess);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    // updates the listview ui
    public void updateMessagesListView()
    {
        arrAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myMess);
        registrationListView.setAdapter(arrAdap);
    }

    // deletes declined message from listview
    public void messageDeclined(int itemChosen)
    {
        myMess.remove(itemChosen);
        updateMessagesListView();
    }

    // changes message status to accepted, deletes all messages from listview
    public void messageAccepted(int itemChosen)
    {
        // todo changes status of message to accepted on database
        deleteAllInviteMessages();
        updateMessagesListView();
        Toast.makeText(this, "Invitation has been accepted Successfully!", Toast.LENGTH_SHORT).show();
    }

    public void deleteAllInviteMessages()
    {
        myMess.clear();
    }
}
