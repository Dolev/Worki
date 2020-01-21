package edu.ariel.SE_project.worki.worker_to_company_registration;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.ariel.SE_project.worki.assistance_classes.GlobalMetaData;
import edu.ariel.SE_project.worki.assistance_classes.MyNotificationHandler;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;
import edu.ariel.SE_project.worki.data.User;

public class MessagesHandler
{
    private static MessagesHandler instance = new MessagesHandler();
    private static Activity activity;

    private HashMap<String, InviteMessage> messages = new HashMap<>();

    // needs changes
    private List<Consumer<List<InviteMessage>>> listeners = new LinkedList<>();

    public static MessagesHandler getInstance()
    {
        return instance;
    }

    public static void setContext(Activity activity)
    {
        MessagesHandler.activity = activity;
    }

    private MessagesHandler()
    {
        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {
                if (user.email == null)
                    throw new RuntimeException("email is null");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference(GlobalMetaData.messagesPath(user.email));
                myRef.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        InviteMessage inviteMessage = new InviteMessage().readFromDatabase(dataSnapshot);
                        messages.put(inviteMessage.getSender(), inviteMessage);
                        Log.d("MessagesHandler", "Message Recived: " + inviteMessage);
                        onChange();

                        if (CurrentUser.getInstance().getUserData() != null && CurrentUser.getInstance().getUserData().isManager)
                        {
                            if (inviteMessage.getCurrentStatus() == InviteMessage.InvitationStatus.accepted_not_opened)
                                invitationAccepted(inviteMessage);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
//                        InviteMessage inviteMessage = new InviteMessage().readFromDatabase(dataSnapshot);
//                        messages.put(inviteMessage.getSender(), inviteMessage);
//                        onChange();

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {
                        InviteMessage inviteMessage = new InviteMessage().readFromDatabase(dataSnapshot);
                        messages.remove(inviteMessage.getRecipient());
                        onChange();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
        });
    }

    private void invitationAccepted(InviteMessage message)
    {
        Log.d("MessagesHandler", "Message accepted: " + message.toString().replace('\n', ' '));
        CurrentUser.getInstance().addWorkerToCompany(message.getSender());
        message.setCurrentStatus(InviteMessage.InvitationStatus.accepted);
        updateMessage(message);
        MyNotificationHandler.createNotification(activity, "Invitation Accepted", message.getSender() + " has accepted your invitation");

    }

    public List<InviteMessage> getMessages()
    {
        return new ArrayList<>(messages.values());
    }

    public void addOnMessagesChangedListener(Consumer<List<InviteMessage>> listener)
    {
        listeners.add(listener);
    }

    private void onChange()
    {
        List<InviteMessage> messages = getMessages();
        for (Consumer<List<InviteMessage>> listener : listeners)
        {
            listener.accept(messages);
        }
    }

    public void sendMessage(InviteMessage message)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath(message.getRecipient())).push();
        message.writeToDatabase(ref);
    }

    public void updateMessage(InviteMessage message)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(GlobalMetaData.messagesPath(message.getRecipient())).child(message.getId());
        message.writeToDatabase(ref);
    }

    private static String createReply(String sender, String status, boolean invite)
    {
        if (invite)
        {
            return "Invitation from " + sender + ".\nStatus: " + status;
        }
        return sender + " has " + status + " your Invitation.";
    }

    public static List<String> convertToStrings(List<InviteMessage> inviteMessages)
    {
        List<String> repliesInStrings = new ArrayList<>();
        for (InviteMessage im : inviteMessages)
        {
            if (im.isShow())
                repliesInStrings.add(createReply(im.getSender(), im.statusToString(), im.isInvite()));
        }

        return repliesInStrings;
    }
}
