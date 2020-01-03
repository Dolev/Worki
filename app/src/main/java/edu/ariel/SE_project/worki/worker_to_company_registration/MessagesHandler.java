package edu.ariel.SE_project.worki.worker_to_company_registration;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ariel.SE_project.worki.data.InviteMessage;

public class MessagesHandler
{
    public static HashMap<String, ArrayList<InviteMessage>> inviteWorkers = new HashMap<String, ArrayList<InviteMessage>>();
    public static HashMap<String, ArrayList<InviteMessage>> workerReplies = new HashMap<String, ArrayList<InviteMessage>>();



    public static void insertByKey(HashMap<String, ArrayList<InviteMessage>> hMap, String userId, InviteMessage inviteMessage)
    {
        if(hMap.containsKey(userId))
        {
            hMap.get(userId).add(inviteMessage);
        }
        else
        {
            ArrayList<InviteMessage> list = new ArrayList<>();
            list.add(inviteMessage);
            hMap.put(userId, list);
        }
    }

    public static void deleteUserMessages(HashMap<String, ArrayList<InviteMessage>> hMap, String userId)
    {
        if(hMap.containsKey(userId))
        {
            hMap.remove(userId);
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

        insertByKey(workerReplies, managerId, sendToManager);

    }

    private static String createReply(String sender, String reply)
    {
        String answer = sender + "has" + reply + "your Invitation.";
        return answer;
    }

    public static ArrayList<String> convertToStrings (ArrayList<InviteMessage> inviteMessages)
    {
        ArrayList<String> repliesInStrings = new ArrayList<>();
        for (InviteMessage im: inviteMessages)
        {
            repliesInStrings.add(createReply(im.getSender(), im.statusToString()));
        }

        return repliesInStrings;
    }

}
