package edu.ariel.SE_project.worki.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

/**
 * An object to store user data.
 */
public class User
{
    public int id;
    public String name;
    public boolean isManager;

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
     * Create a user using a data snapshot of the storage.
     *
     * @param snapshot the DataSnapshot of the storage.
     */
    User(FirebaseUser user, DataSnapshot snapshot)
    {

        id = Integer.parseInt(user.getUid());

        name = snapshot.child("name").getValue(String.class);
        Boolean manager = snapshot.child("manager").getValue(boolean.class);
        if (manager != null)
            isManager = manager;

    }
}
