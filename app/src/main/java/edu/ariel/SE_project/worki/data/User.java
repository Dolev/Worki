package edu.ariel.SE_project.worki.data;

import com.google.firebase.auth.FirebaseUser;

/**
 * An object to store user data.
 */
public class User
{
    public int id;
    public String name;

    /**
     * Create an empty user object.
     */
    public User()
    {

    }

    /**
     * Create a new user object.
     *
     * @param id   the users ID (firebase's .getUid).
     * @param name the name of the user.
     */
    public User(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /**
     * Read user data from Firebase.
     *
     * @param user the firebase user.
     */
    public User(FirebaseUser user)
    {
        id = Integer.parseInt(user.getUid());
        name = user.getDisplayName();
    }
}
