package edu.ariel.SE_project.worki.data;

import com.google.firebase.auth.FirebaseUser;

public class User
{
    public int id;
    public String name;

    public User()
    {

    }

    public User(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public User(FirebaseUser user)
    {
        id = Integer.parseInt(user.getUid());
        name = user.getDisplayName();
    }
}
