package edu.ariel.SE_project.worki.worker_to_company_registration;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ariel.SE_project.worki.data.InviteMessage;

public class MessagesHandler
{
    public static HashMap<String, ArrayList<InviteMessage>> inviteWorkers = new HashMap<>();
    public static HashMap<String, ArrayList<InviteMessage>> workerReplies = new HashMap<>();

    public static void sendMessage(boolean manager, String recipient, InviteMessage message)
    {
        HashMap<String, ArrayList<InviteMessage>> hMap;
        if (manager)
            hMap = inviteWorkers;
        else
            hMap = workerReplies;

        if (hMap.containsKey(recipient))
        {
            if (hMap.get(recipient) != null)
                hMap.get(recipient).add(message);
        } else
        {
            ArrayList<InviteMessage> list = new ArrayList<>();
            list.add(message);
            hMap.put(recipient, list);
        }
    }

    public static void deleteMessage(boolean manager, String recipient)
    {
        HashMap<String, ArrayList<InviteMessage>> hMap;
        if (manager)
            hMap = inviteWorkers;
        else
            hMap = workerReplies;

        if (hMap.containsKey(recipient))
        {
            hMap.remove(recipient);
        }

    }

    public static void sendInviteToWorker(InviteMessage inviteMessage)
    {
        InviteMessage sendToWorker = new InviteMessage(inviteMessage);
    }

    public static void sendReplyToManager(InviteMessage inviteMessage)
    {
        InviteMessage sendToManager = new InviteMessage(inviteMessage);
        String managerId = inviteMessage.getSenderId();
        sendToManager.inverseRecipientSender();

        sendMessage(false, managerId, sendToManager);

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
