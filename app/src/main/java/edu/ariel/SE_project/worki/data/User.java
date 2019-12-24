package edu.ariel.SE_project.worki.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * An object to store user data.
 */
public class User
{
    public String id;
    public String name;
    public String email;
    public boolean isManager;
    public String companyId;

    /**
     * Create an empty user object.
     */
    public User()
    {

    }

    /**
     * Create a user using a data snapshot of the storage.
     *
     * @param snapshot the DataSnapshot of the storage.
     */
    User(FirebaseUser user, DataSnapshot snapshot)
    {
        id = user.getUid();

        name = snapshot.child("name").getValue(String.class);
        Boolean manager = snapshot.child("manager").getValue(boolean.class);
        if (manager != null)
            isManager = manager;

    }

    /**
     * Create a new user object.
     *
     * @param id   the users ID (firebase's .getUid).
     * @param name the name of the user.
     */
    public User(String id, String name, String email, boolean isManager, String companyId)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isManager = isManager;
        this.companyId = companyId;
    }

    public void writeToDatabase(DatabaseReference reference)
    {

        reference.child("name").setValue(name);
        reference.child("email").setValue(email);

        if (isManager)
        {
            reference.child("manager").setValue(true);
            reference.child("companyId").setValue(companyId);
        } else
        {
            reference.child("manager").setValue(false);
        }
    }
}
