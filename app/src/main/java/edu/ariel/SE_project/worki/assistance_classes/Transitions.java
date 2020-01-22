package edu.ariel.SE_project.worki.assistance_classes;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.ariel.SE_project.worki.MainActivity;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.User;
import edu.ariel.SE_project.worki.login_register.LoginRegisterActivity;
import edu.ariel.SE_project.worki.login_register.RegisterCompany;
import edu.ariel.SE_project.worki.signed_in_activities.TimerActivity;
import edu.ariel.SE_project.worki.worker_to_company_registration.RegisterWorkerToCompanyActivity;

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

        Log.d("Transitions", "Going to Login activity. login: " + login + ", asManager: "
                + asManager + ", Caller: " + activity);

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
     */
    public static void toLoggedInActivity(final Activity activity)
    {
        Log.d("Transitions", "Going to Logged In activity. Caller: " + activity);

        final String TAG = "To Logged In Activity";

        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(final User user)
            {
                if (user.isManager)
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(GlobalMetaData.companiesPath + '/' + user.id);
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if (snapshot.getValue() == null)
                            {
                                Log.d("Transitions", "Unregistered Company!");
                                toRegisterCompany(activity);
                            } else
                            {
                                Intent intent = new Intent(activity, RegisterWorkerToCompanyActivity.class);

                                intent.putExtra("isManager", user.isManager);

                                activity.startActivity(intent);

                                activity.finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

                        }
                    });

                } else
                {
                    Intent intent = new Intent(activity, TimerActivity.class);

                    intent.putExtra("isManager", user.isManager);

                    activity.startActivity(intent);

                    activity.finish();
                }
            }
        });
    }

    public static void toRegisterCompany(final Activity activity)
    {
        Log.d("Transitions", "Going to Register Company activity. Caller: " + activity);

        activity.startActivity(new Intent(activity, RegisterCompany.class));
        activity.finish();
    }

    public static void toMainActivity(Activity activity)
    {

        Log.d("Transitions", "Going to Main activity. Caller: " + activity);

        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public static Intent getNotificationIntent(Activity activity)
    {
        Intent intent = new Intent(activity, RegisterWorkerToCompanyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("isManager", true);
        return intent;
    }
}
