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
    private String senderId;
    private String sender;


    public InviteMessage()
    {

    }

    public InviteMessage (InviteMessage inviteMessage)
    {
        this.recipient = inviteMessage.getRecipient();
        this.sender = inviteMessage.getSender();
        this.senderId = inviteMessage.getSenderId();
        this.currentStatus = inviteMessage.getCurrentStatus();
    }

//    public  InviteMessage (String recipient, String sender)
//    {
//        this.recipient = recipient;
//        this.sender = sender;
//    }

    public InviteMessage(String recipient, String sender, String senderId)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.senderId = senderId;
        this.currentStatus = invitationStatus.undecided;
    }

    public InviteMessage(String recipient, String sender, String senderId, invitationStatus currentStatus)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.senderId = senderId;
        this.currentStatus = currentStatus;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public String getSender()
    {
        return sender;
    }

    public invitationStatus getCurrentStatus()
    {
        return currentStatus;
    }

    public void setCurrentStatus(invitationStatus currentStatus)
    {
        this.currentStatus = currentStatus;
    }


    public String getSenderId()
    {
        return senderId;
    }

    public void inverseRecipientSender()
    {
        String temp = this.recipient;
        this.recipient = this.sender;
        this.sender = temp;
    }

    public String statusToString()
    {
        if(this.getCurrentStatus() == invitationStatus.accepted)
        {
            return "Accepted";
        }
        else if(this.getCurrentStatus() == InviteMessage.invitationStatus.declined)
        {
            return "Declined";
        }

        return "Undecided";         // might require a change
    }

    /**
     * Read a object from the database.
     *
     * @param snapshot the database snapshot where the object is stored.
     */
    @Override
    public ReadableFromDatabase readFromDatabase(DataSnapshot snapshot)
    {

        String recipient = snapshot.child("recipient").getValue(String.class);
        String sender = snapshot.child("sender").getValue(String.class);
        String senderId = snapshot.child("senderId").getValue(String.class);
        invitationStatus invitationStatus = snapshot.child("invitationStatus").getValue(InviteMessage.invitationStatus.class);

        return new InviteMessage(recipient, sender, senderId, invitationStatus);

    }

    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        reference.child("recipient").setValue(recipient);
        reference.child("sender").setValue(sender);
        reference.child("senderId").setValue(senderId);
        reference.child("invitationStatus").setValue(currentStatus);
    }
}
