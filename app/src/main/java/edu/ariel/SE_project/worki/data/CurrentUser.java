package edu.ariel.SE_project.worki.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for getting user data from database. Data is updated automatically.
 * TODO won't work when logging out
 */
public class CurrentUser
{

    /**
     * The User object.
     */
    private User user;

    private FirebaseUser firebaseUser;

    public List<Consumer<User>> listeners = new LinkedList<>();

    /**
     * The singleton instance.
     */
    private static final CurrentUser ourInstance = new CurrentUser();

    /**
     * Get singleton instance.
     *
     * @return singleton instance.
     */
    public static CurrentUser getInstance()
    {
        return ourInstance;
    }

    /**
     * Create the user and add the data listeners.
     */
    private CurrentUser()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                updateUser(firebaseAuth.getCurrentUser());

            }
        });
        updateUser(firebaseUser);

    }

    /**
     * Update the user data.
     *
     * @param firebaseUser the current signed in user, null when not signed in ( will return without doing anything).
     */
    private void updateUser(final FirebaseUser firebaseUser)
    {
        if (firebaseUser == null)
            return;


        this.firebaseUser = firebaseUser;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user/" + firebaseUser.getUid());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = new User(firebaseUser, dataSnapshot);


                for (Consumer<User> listener : listeners)
                {
                    listener.accept(user);
                    listeners.remove(listener);
                }

                Log.d("CurrentUser", "Reading Data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Failed to read value
                Log.w("CurrentUser", "Failed to read data.", error.toException());
            }
        });
    }

    /**
     * Get user data.
     *
     * @return the most recent version of the user data.
     */
    public User getUserData()
    {
        if (user == null)
        {

        }
        return user;
    }

    public FirebaseUser getFirebaseUser()
    {
        return firebaseUser;
    }

    public void addOnUserNotNullListener(Consumer<User> listener)
    {
        if (user != null)
            listener.accept(user);
        else
            listeners.add(listener);
    }
}
