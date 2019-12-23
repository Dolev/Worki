package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);


        login.setOnClickListener(this);
        registerWorker.setOnClickListener(this);
        registerCompany.setOnClickListener(this);


    }

    private void updateUI(FirebaseUser currentUser)
    {
        Transitions.toLoggedInActivity(this, currentUser);
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

    // This method allows the user to navigate on the app through the menu
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        Intent intent;
//        switch (item.getItemId()){
//            case R.id.toolbarHomePage:{
//                intent = new Intent(MainActivity.this, TimerActivity.class);
//                startActivity(intent);
//                return true;
//            }
//            case R.id.toolbarAskForShifts:{
//                intent = new Intent(MainActivity.this, ShiftsActivity.class);
//                startActivity(intent);
//                return true;
//            }
//            case R.id.toolbarWhosOnline:{
//                intent = new Intent(MainActivity.this, OnlineActivity.class);
//                startActivity(intent);
//                return true;
//            }
//            case R.id.toolbarSettings:{
//                intent = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                return true;
//            }
//            case R.id.toolbarLogout:{
//                intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
