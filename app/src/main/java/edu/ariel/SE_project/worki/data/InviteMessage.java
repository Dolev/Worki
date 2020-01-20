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
        undecided, accepted, declined
    }

    private InvitationStatus currentStatus;
    private String recipient;
    private String sender;

    public boolean isInvite()
    {
        return invite;
    }

    private boolean invite;
    private String companyId;
    private boolean show = true;

    public boolean isShow()
    {
        return show;
    }

    public String getCompanyId()
    {
        return companyId;
    }

    public String getId()
    {
        return id;
    }

    private String id;


    public InviteMessage()
    {

    }

    public InviteMessage(InviteMessage inviteMessage)
    {
        this.recipient = inviteMessage.getRecipient();
        this.sender = inviteMessage.getSender();
        this.currentStatus = inviteMessage.getCurrentStatus();
        this.id = inviteMessage.id;
        this.invite = inviteMessage.invite;
        this.companyId = inviteMessage.companyId;
        this.show = inviteMessage.show;
    }

//    public  InviteMessage (String recipient, String sender)
//    {
//        this.recipient = recipient;
//        this.sender = sender;
//    }

    public InviteMessage(String recipient, String sender, InvitationStatus status, boolean invite, String companyId)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.currentStatus = status;
        this.invite = invite;
        this.companyId = companyId;
    }

    public InviteMessage(String recipient, String sender, String id, InvitationStatus status, boolean invite, String companyId, boolean show)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.currentStatus = status;
        this.id = id;
        this.invite = invite;
        this.companyId = companyId;
        this.show = show;
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


    public InviteMessage replyMessage()
    {
        InviteMessage mess = new InviteMessage(this);
        mess.recipient = this.sender;
        mess.sender = this.recipient;
        mess.invite = !this.invite;
        return mess;
    }

    public String statusToString()
    {
        if (this.getCurrentStatus() == InvitationStatus.accepted)
        {
            return "Accepted";
        } else if (this.getCurrentStatus() == InvitationStatus.declined)
            return "Declined";
        else return "Undecided";
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
        boolean invite = getOrDefault(snapshot.child("invite").getValue(Boolean.class), false);
        String id = snapshot.getKey();
        String companyId = snapshot.child("id").getValue(String.class);
        boolean show = getOrDefault(snapshot.child("show").getValue(Boolean.class), true);

        return new InviteMessage(recipient, sender, id, invitationStatus, invite, companyId, show);

    }

    private boolean getOrDefault(Boolean nullable, boolean def)
    {
        if (nullable == null)
            return def;
        else return nullable;
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
        reference.child("invite").setValue(invite);
        reference.child("id").setValue(companyId);
        reference.child("show").setValue(show);
    }

    public InviteMessage hidden()
    {
        InviteMessage mess = new InviteMessage(this);

        mess.show = false;
        return mess;
    }
}
