package edu.ariel.SE_project.worki.data;

public class InviteMessageObj
{
    public String recipient;
    public String sender;

    public InviteMessageObj()
    {
    }

    public InviteMessageObj(String recipient, String sender)
    {
        this.recipient = recipient;
        this.sender = sender;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public String getSender()
    {
        return sender;
    }

}
