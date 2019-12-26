package edu.ariel.SE_project.worki.assistance_classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ariel.SE_project.worki.data.CurrentUser;

public class GlobalMetaData
{
    public static final String usersPath = "users";
    public static final String companiesPath = "companies";
    public static final String messagesPath = "messages";

    public static String userDataPath()
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
            return "users/" + user.getUid();
        else
        {
            throw new RuntimeException("User not logged in");
        }
    }

    public static String companyDataPath()
    {
        return "companies/" + CurrentUser.getInstance().getUserData().companyId;
    }
}
