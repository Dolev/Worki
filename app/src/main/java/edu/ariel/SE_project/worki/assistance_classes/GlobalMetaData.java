package edu.ariel.SE_project.worki.assistance_classes;

import edu.ariel.SE_project.worki.data.CurrentUser;

public class GlobalMetaData
{
    public static String userDataPath()
    {
        return "users/" + CurrentUser.getInstance().getUserData().id;
    }

    public static String companyDataPath()
    {
        return "companies/" + CurrentUser.getInstance().getUserData().companyId;
    }
}
