package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ariel.SE_project.worki.ui.login.LoginRegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button login;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        register = findViewById(R.id.reg);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);


        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void updateUI(FirebaseUser currentUser)
    {

        // TODO start signed in activity
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(this, LoginRegisterActivity.class);

        if (v == login)
        {
            intent.putExtra("register", false);
        } else if (v == register)
        {
            intent.putExtra("register", true);
        }

        startActivity(intent);
    }
}
