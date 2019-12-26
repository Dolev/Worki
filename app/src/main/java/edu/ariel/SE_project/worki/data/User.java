package edu.ariel.SE_project.worki.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private List<ShiftStamp> shiftStamps = new ArrayList<>();

    /**
     * Create an empty user object.
     */
    public User()
    {

    }

    /**
     * Create a user using a data snapshot of the storage.
     *
     * @param snapshot the snapshot of the database where the User is stored.
     */
    User(FirebaseUser user, DataSnapshot snapshot)
    {
        id = user.getUid();

        name = snapshot.child("name").getValue(String.class);
        Boolean manager = snapshot.child("manager").getValue(boolean.class);
        if (manager != null)
            isManager = manager;
        else
            isManager = false;

        companyId = snapshot.child("companyId").getValue(String.class);

        for (DataSnapshot d : snapshot.child("shiftStamps").getChildren())
        {
            shiftStamps.add(new ShiftStamp(d));
        }
        Collections.sort(shiftStamps, (new Comparator<ShiftStamp>()
        {
            @Override
            public int compare(ShiftStamp o1, ShiftStamp o2)
            {
                //noinspection UseCompareMethod
                return ((Long) o1.utcTime).compareTo(o2.utcTime);
            }
        }));
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

    /**
     * Store this User in the database.
     * Warning: this will delete any user data at this location
     *
     * @param reference the database reference where the User would be stored.
     */
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


        DatabaseReference stamps = reference.child("shiftStamps");
        stamps.removeValue();
        for (ShiftStamp s : shiftStamps)
        {
            s.writeToDatabase(stamps.push());
        }
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isManager=" + isManager +
                ", companyId='" + companyId + '\'' +
                '}';
    }
}
