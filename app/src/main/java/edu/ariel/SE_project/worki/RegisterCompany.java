package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Register a company.
 */
public class RegisterCompany extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private EditText name;

    private EditText address;
    private EditText phone;

    private Button enterButton;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);


        // Initialize variables
        name = findViewById(R.id.companyName);

        address = findViewById(R.id.companyPostalAddress);
        phone = findViewById(R.id.companyPhoneNumber);

        enterButton = findViewById(R.id.companyRegisterButton);

        loadingProgressBar = findViewById(R.id.comapnyRegisterProgressBar);
        loadingProgressBar.setVisibility(View.GONE);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if (user == null)
        {
            // TODO Error!
            return;
        }

        // Called if the user presses enter from password field.
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener()
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
     * Info was entered. Now we register the company
     */
    private void enter()
    {
        if (validateForm())
        {
            loadingProgressBar.setVisibility(View.VISIBLE);


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("companies/" + user.getUid());

            boolean nameSuccessful = myRef.child("name").setValue(name.getText().toString()).isSuccessful();
            boolean addressSuccessful = myRef.child("address").setValue(address.getText().toString()).isSuccessful();
            boolean phoneSuccessful = myRef.child("phone").setValue(phone.getText().toString()).isSuccessful();

            if (nameSuccessful && addressSuccessful && phoneSuccessful)
            {
                updateUI(true);
            } else
            {
                updateUI(false);
            }

        }
    }

    /**
     * Called after registering the company.
     *
     * @param successful was the registration successful?
     */
    private void updateUI(boolean successful)
    {
        if (successful)
            Transitions.toLoggedInActivity(this, user);
    }

    /**
     * Make sure the info was filled before entering it.
     *
     * @return true if the info is valid
     */
    private boolean validateForm()
    {
        boolean valid = true;

        String nm = name.getText().toString();
        if (TextUtils.isEmpty(nm))
        {
            name.setError("Required.");
            valid = false;
        } else
        {
            name.setError(null);
        }

        String addr = address.getText().toString();
        if (TextUtils.isEmpty(addr))
        {
            address.setError("Required.");
            valid = false;
        } else
        {
            address.setError(null);
        }

        String ph = phone.getText().toString();
        if (TextUtils.isEmpty(ph))
        {
            phone.setError("Required.");
            valid = false;
        } else
        {
            phone.setError(null);
        }

        return valid;
    }
}
