package edu.ariel.SE_project.worki.data;

public class InviteMessageObj
{
    public String recipient;
    public String sender;
    public String messageId;

    public InviteMessageObj()
    {
    }

    public InviteMessageObj(String recipient, String sender, String messageId)
    {
        this.recipient = recipient;
        this.sender = sender;
        this.messageId = messageId;
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

}
