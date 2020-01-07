package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ariel.SE_project.worki.assistance_classes.Transitions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button login;
    private Button registerWorker;
    private Button registerCompany;
    private FirebaseAuth mAuth;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        registerWorker = findViewById(R.id.regWorker);
        registerCompany = findViewById(R.id.regCompany);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        checkLogin();

        login.setOnClickListener(this);
        registerWorker.setOnClickListener(this);
        registerCompany.setOnClickListener(this);


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        checkLogin();
    }

    private boolean loggedIn = false;

    private void checkLogin()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && !loggedIn)
        {
            loggedIn = true;
            Transitions.toLoggedInActivity(this);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == login)
        {
            Transitions.toLoginOrRegister(this, true, false);
        } else if (v == registerWorker)
        {
            Transitions.toLoginOrRegister(this, false, false);
        } else if (v == registerCompany)
        {
            Transitions.toLoginOrRegister(this, false, true);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.toolbar_menu, menu);
//        return true;
//    }

}