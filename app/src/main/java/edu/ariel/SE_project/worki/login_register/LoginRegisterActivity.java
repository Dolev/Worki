package edu.ariel.SE_project.worki.login_register;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.assistance_classes.GlobalMetaData;
import edu.ariel.SE_project.worki.assistance_classes.Transitions;
import edu.ariel.SE_project.worki.data.User;




public class LoginRegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    private EditText name;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button enterButton;
    private ProgressBar loadingProgressBar;

    private boolean registering;
    private boolean asManager;

    private String TAG;
    private final int minPasswordLength = 6;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        // Initialize variables
        name = findViewById(R.id.name);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        enterButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get variables from caller
        Intent intent = getIntent();
        registering = intent.getBooleanExtra("register", true);
        asManager = intent.getBooleanExtra("manager", false);

        if (registering)
        {
            enterButton.setText(R.string.action_register);
            setTitle(R.string.register_title);
            if (asManager)
                TAG = "Manager_Register_Email";
            else
                TAG = "Worker_Register_Email";

            name.setVisibility(View.VISIBLE);
            name.setEnabled(true);
        } else
        {
            enterButton.setText(R.string.action_sign_in);
            setTitle(R.string.login_title);
            TAG = "Login";
            name.setVisibility(View.GONE);
            name.setEnabled(false);
        }

        enterButton.setEnabled(true);

        // If we want to do something when the text changes
        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // data changed
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        // Called if the user presses enter from password field.
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    enter();
                }
                return false;
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enter();
            }
        });
    }

    /**
     * Info was entered. Now we log in or register.
     */
    private void enter()
    {
        if (validateForm())
        {
            loadingProgressBar.setVisibility(View.VISIBLE);


            if (registering)
                register(emailEditText.getText().toString(), passwordEditText.getText().toString());
            else
                login(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    /**
     * Login to user to firebase.
     *
     * @param email    E-Mail
     * @param password Password
     */

    private void login(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    /**
     * Register user to firebase.
     *
     * @param email    E-Mail
     * @param password Password
     */
    private void register(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        afterRegister(task);
                        // ...
                    }
                });
    }

    private void afterRegister(@NonNull Task<AuthResult> task)
    {
        if (task.isSuccessful())
        {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = mAuth.getCurrentUser();

            // Update profile with name
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString())
                    .build();
            if (user != null)
            {
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(GlobalMetaData.userDataPath());

            if (user != null)
                new User(user.getUid(), name.getText().toString(),
                        emailEditText.getText().toString(), asManager, asManager ? user.getUid() : null)
                        .writeToDatabase(myRef);

            updateUI(user);
        } else
        {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(LoginRegisterActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    /**
     * Make sure the info was filled before entering it.
     *
     * @return true if the info is valid
     */
    private boolean validateForm()
    {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            emailEditText.setError("Required.");
            valid = false;
        } else
        {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password))
        {
            passwordEditText.setError("Required.");
            valid = false;
        } else if (password.length() < minPasswordLength)
        {
            passwordEditText.setError("Must be more than " + minPasswordLength + " characters.");
            valid = false;
        } else
        {
            passwordEditText.setError(null);
        }

        if (registering)
        {
            String nm = name.getText().toString();
            if (TextUtils.isEmpty(nm))
            {
                name.setError("Required.");
                valid = false;
            } else
            {
                name.setError(null);
            }
        }

        return valid;
    }

    /**
     * Called after login / register is finished.
     *
     * @param currentUser current user data
     */
    private void updateUI(FirebaseUser currentUser)
    {

        loadingProgressBar.setVisibility(View.GONE);
        if (currentUser != null)
        {
            if (registering)
            {
                Toast.makeText(getBaseContext(), "Registered as: " + currentUser.getEmail(),
                        Toast.LENGTH_LONG).show();

                if (asManager)
                {
                    Intent intent = new Intent(this, RegisterCompany.class);
                    startActivity(intent);
                } else
                {
                    Transitions.toLoggedInActivity(this);
                }
            } else
            {
                Toast.makeText(getBaseContext(), "Logged in as: " + currentUser.getEmail(),
                        Toast.LENGTH_LONG).show();

                Transitions.toLoggedInActivity(this);
            }
        }
    }
}