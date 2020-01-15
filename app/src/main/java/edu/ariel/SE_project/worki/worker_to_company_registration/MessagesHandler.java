package edu.ariel.SE_project.worki.worker_to_company_registration;

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
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.InviteMessage;
import edu.ariel.SE_project.worki.data.User;

public class MessagesHandler
{
    public static HashMap<String, ArrayList<InviteMessage>> inviteWorkers = new HashMap<>();
    public static HashMap<String, ArrayList<InviteMessage>> workerReplies = new HashMap<>();

//    private static MessagesHandler instance = new MessagesHandler();
//
//    private HashMap<String, InviteMessage> messages = new HashMap<>();
//
//    // needs changes
//    public List<Consumer<List<InviteMessage>>> listeners = new LinkedList<>();
//
//    public static MessagesHandler getInstance()
//    {
//        return instance;
//    }
//
//    private MessagesHandler()
//    {
//        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
//        {
//            @Override
//            public void accept(User user)
//            {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference(GlobalMetaData.usersPath + '/' + user.id);
//                myRef.addChildEventListener(new ChildEventListener()
//                {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
//                    {
//                        InviteMessage inviteMessage = new InviteMessage().readFromDatabase(dataSnapshot);
//                        messages.put(inviteMessage.getRecipient(), inviteMessage);
//                        onChange();
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
//                    {
//                        InviteMessage inviteMessage = new InviteMessage().readFromDatabase(dataSnapshot);
//                        messages.put(inviteMessage.getRecipient(), inviteMessage);
//                        onChange();
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
//                    {
//                        InviteMessage inviteMessage = new InviteMessage().readFromDatabase(dataSnapshot);
//                        messages.remove(inviteMessage.getRecipient());
//                        onChange();
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
//                    {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError)
//                    {
//
//                    }
//                });
//            }
//        });
//    }
//
//    public List<InviteMessage> getMessages()
//    {
//        return new ArrayList<>(messages.values());
//    }
//
//    public void addOnMessagesChangedListener(Consumer<List<InviteMessage>> listener)
//    {
//        listeners.add(listener);
//    }
//
//    private void onChange()
//    {
//        List<InviteMessage> messages = getMessages();
//        for (Consumer<List<InviteMessage>> listener : listeners)
//        {
//            listener.accept(messages);
//        }
//    }

    public static void sendMessage(boolean manager, InviteMessage message)
    {
        HashMap<String, ArrayList<InviteMessage>> hMap;
        if (manager)
            hMap = inviteWorkers;
        else
            hMap = workerReplies;

        if (hMap.containsKey(message.getRecipient()))
        {
            if (hMap.get(message.getRecipient()) != null)
                hMap.get(message.getRecipient()).add(message);
        } else
        {
            ArrayList<InviteMessage> list = new ArrayList<>();
            list.add(message);
            hMap.put(message.getRecipient(), list);
        }
    }

    public static void deleteMessage(boolean manager, InviteMessage inviteMessage)
    {
        HashMap<String, ArrayList<InviteMessage>> hMap;
        if (manager)
            hMap = inviteWorkers;
        else
            hMap = workerReplies;

        if (hMap.containsKey(inviteMessage.getRecipient()))
        {
            hMap.get(inviteMessage.getRecipient()).remove(inviteMessage);
        }

    }


    public static void sendReplyToManager(InviteMessage inviteMessage)
    {
        InviteMessage sendToManager = new InviteMessage(inviteMessage);
        sendToManager.inverseRecipientSender();

        sendMessage(false, sendToManager);

    }

    private static String createReply(String sender, String reply)
    {
        String answer = sender + "has" + reply + "your Invitation.";
        return answer;
    }

    public static ArrayList<String> convertToStrings(ArrayList<InviteMessage> inviteMessages)
    {
        ArrayList<String> repliesInStrings = new ArrayList<>();
        for (InviteMessage im : inviteMessages)
        {
            repliesInStrings.add(createReply(im.getSender(), im.statusToString()));
        }

        return repliesInStrings;
    }

}
