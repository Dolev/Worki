package edu.ariel.SE_project.worki.assistance_classes;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import edu.ariel.SE_project.worki.MainActivity;
import edu.ariel.SE_project.worki.TimerActivity;
import edu.ariel.SE_project.worki.login_register.LoginRegisterActivity;

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
    public static void toLoginOrRegister(Activity activity, boolean login, boolean asManager)
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
    public static void toLoggedInActivity(Activity activity, FirebaseUser user)
    {

        final String TAG = "To Logged In Activity";

        activity.startActivity(new Intent(activity, TimerActivity.class));

        activity.finish();
    }

    public static void toMainActivity(Activity activity)
    {

        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
}
