package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class InviteMessage implements ReadableFromDatabase, WriteableToDatabase

{
    public enum invitationStatus
    {
        undecided, accepted, declined
    }

    public invitationStatus currentStatus;
    public String recipient;
    public String sender;
    public String messageId;


    public InviteMessage()
    {

    }

    public InviteMessage(String recipient, String sender, String messageId)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.messageId = messageId;
        this.currentStatus = invitationStatus.undecided;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public String getSender()
    {
        return sender;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void writeMessageToDatabase()
    {

    }

    /**
     * Read a object from the database.
     *
     * @param snapshot the database snapshot where the object is stored.
     */
    @Override
    public ReadableFromDatabase readFromDatabase(DataSnapshot snapshot)
    {
        //todo status?
        String recipient = snapshot.child("recipient").getValue(String.class);
        String sender = snapshot.child("sender").getValue(String.class);
        String messageId = snapshot.child("messageId").getValue(String.class);

        return new InviteMessage(recipient, sender,messageId);
    }

    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        //todo status?
        reference.child("recipient").setValue(recipient);
        reference.child("sender").setValue(sender);
        reference.child("messageId").setValue(messageId);
    }
}
