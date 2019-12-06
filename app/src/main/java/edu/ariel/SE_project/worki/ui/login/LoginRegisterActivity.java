package edu.ariel.SE_project.worki.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
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

import edu.ariel.SE_project.worki.R;

public class LoginRegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button enterButton;
    private ProgressBar loadingProgressBar;

    private boolean to_register;

    private String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        // Initialize variables
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        enterButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get variables from caller
        Intent intent = getIntent();
        to_register = intent.getBooleanExtra("register", true);

        if (to_register)
        {
            enterButton.setText(R.string.action_register);
            setTitle(R.string.register_title);
            TAG = "Register_Email";
        } else
        {
            enterButton.setText(R.string.action_sign_in);
            setTitle(R.string.login_title);
            TAG = "Login";
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
                if (validateForm())
                {
                    enter();
                }
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


            if (to_register)
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
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
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
        } else
        {
            passwordEditText.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser currentUser)
    {

        loadingProgressBar.setVisibility(View.GONE);
        if (currentUser != null)
        {
            if (to_register)
            {
                Toast.makeText(getBaseContext(), "Registered as: " + currentUser.getEmail(),
                        Toast.LENGTH_LONG).show();

                // TODO finish registration (FullRegister)
            } else
            {
                Toast.makeText(getBaseContext(), "Logged in as: " + currentUser.getEmail(),
                        Toast.LENGTH_LONG).show();

                // TODO go to logged in activity
            }
        }
    }
}