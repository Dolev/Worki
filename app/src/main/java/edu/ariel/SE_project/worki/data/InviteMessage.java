package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class InviteMessage implements ReadableFromDatabase, WriteableToDatabase

{

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InviteMessage that = (InviteMessage) o;
        return recipient.equals(that.recipient) &&
                currentStatus.equals(that.currentStatus) &&
                sender.equals(that.sender);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;

        int result = 1;

        result = prime * result + ((recipient == null) ? 0 : recipient.hashCode());

        result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());

        result = prime * result + ((sender == null) ? 0 : sender.hashCode());

        return result;
    }

    public enum InvitationStatus
    {
        invite, accepted, declined
    }

    private InvitationStatus currentStatus;
    private String recipient;
    private String sender;


    public InviteMessage()
    {

    }

    public InviteMessage(InviteMessage inviteMessage)
    {
        this.recipient = inviteMessage.getRecipient();
        this.sender = inviteMessage.getSender();
        this.currentStatus = inviteMessage.getCurrentStatus();
    }

//    public  InviteMessage (String recipient, String sender)
//    {
//        this.recipient = recipient;
//        this.sender = sender;
//    }

    public InviteMessage(String recipient, String sender, InvitationStatus status)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.currentStatus = status;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public String getSender()
    {
        return sender;
    }

    public InvitationStatus getCurrentStatus()
    {
        return currentStatus;
    }

    public void setCurrentStatus(InvitationStatus currentStatus)
    {
        this.currentStatus = currentStatus;
    }


    public void inverseRecipientSender()
    {
        String temp = this.recipient;
        this.recipient = this.sender;
        this.sender = temp;
    }

    public String statusToString()
    {
        if (this.getCurrentStatus() == InvitationStatus.accepted)
        {
            return "Accepted";
        } else
            return "Declined";
        }



    /**
     * Read a object from the database.
     *
     * @param snapshot the database snapshot where the object is stored.
     */
    @Override
    public InviteMessage readFromDatabase(DataSnapshot snapshot)
    {

        String recipient = snapshot.child("recipient").getValue(String.class);
        String sender = snapshot.child("sender").getValue(String.class);
        InvitationStatus invitationStatus = snapshot.child("invitationStatus").getValue(InviteMessage.InvitationStatus.class);

        return new InviteMessage(recipient, sender, invitationStatus);

    }

    /**
     * Store this InviteMessage in the database.
     *
     * @param reference the database reference where the User would be stored.
     */
    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        reference.child("recipient").setValue(recipient);
        reference.child("sender").setValue(sender);
        reference.child("invitationStatus").setValue(currentStatus);
    }
}
