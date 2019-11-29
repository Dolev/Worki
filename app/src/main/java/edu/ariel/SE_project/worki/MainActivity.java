package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import edu.ariel.SE_project.worki.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity
{

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(Helper.ClickTransfer(this, new Intent(this, LoginActivity.class)));
    }
}
