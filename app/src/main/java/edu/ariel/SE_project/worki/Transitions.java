package edu.ariel.SE_project.worki;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Class for all transitions between activities for simplicity.
 */
public class Transitions
{
    /**
     * Go to Login Register Activity.
     *
     * @param activity  the activity.
     * @param login     login?
     * @param asManager as manager?
     */
    public static void toLoginOrRegister(AppCompatActivity activity, boolean login, boolean asManager)
    {
        Intent intent = new Intent(activity, LoginRegisterActivity.class);

        if (login)
        {
            intent.putExtra("register", false);
        } else
        {
            intent.putExtra("register", true);
            intent.putExtra("manager", asManager);
        }

        activity.startActivity(intent);
    }

    /**
     * Go to logged in activity.
     *
     * @param activity the activity.
     * @param user     the firebase user to determine if the user is a manager.
     */
    public static void toLoggedInActivity(AppCompatActivity activity, FirebaseUser user)
    {

        final String TAG = "To Logged In Activity";

        // TODO implement

        activity.finish();
    }
}
