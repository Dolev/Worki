package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class InviteMessage implements ReadableFromDatabase, WriteableToDatabase

{
    public enum invitationStatus
    {
        undecided, accepted, declined
    }

    private invitationStatus currentStatus;
    private String recipient;
    private String sender;



    public InviteMessage()
    {

    }


    public InviteMessage(String recipient, String sender)
    {
        this.recipient = recipient;
        this.sender = sender;
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

    public void setCurrentStatus(invitationStatus currentStatus)
    {
        this.currentStatus = currentStatus;
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

        return new InviteMessage(recipient, sender);
    }

    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        //todo status?
        reference.child("recipient").setValue(recipient);
        reference.child("sender").setValue(sender);
    }
}
