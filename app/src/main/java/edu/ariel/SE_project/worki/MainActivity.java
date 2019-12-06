package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ariel.SE_project.worki.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity
{

    Button login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(Helper.ClickTransfer(this, new Intent(this, LoginActivity.class)));
    }

    private void updateUI(FirebaseUser currentUser)
    {
        // TODO start signed in activity
    }
}
