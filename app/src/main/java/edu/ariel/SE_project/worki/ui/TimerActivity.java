package edu.ariel.SE_project.worki.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.ariel.SE_project.worki.R;

import static edu.ariel.SE_project.worki.R.id.startShift;

public class TimerActivity extends AppCompatActivity
{
    private Button startShift;

    private Button stopShift;
    private Button pauseShift;
    private TextView timer;
    long time = 0;
    long startTime = 0;
    Timer timing = new Timer("MyTimer");//create a new TimerActivity

    //timer:
    final TimerTask timerTask = new TimerTask()
    {

        @Override
        public void run()
        {
            time = startTime - System.currentTimeMillis();
            System.out.println("TimerTask executing counter is: " + counter);
            counter++;//increments the counter
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

// Initialize variables
        startShift = findViewById(R.id.startShift);

        stopShift = findViewById(R.id.stopShift);

        pauseShift= findViewById(R.id.pauseShift);

        timer = findViewById(R.id.timer);

//start shift button
        startShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startTime = System.currentTimeMillis();
                timing.scheduleAtFixedRate(timerTask, 30, 3000);//this line starts the timer at the same time its executed

            }
        });
        //pause shift button
        pauseShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startTime = System.currentTimeMillis();
                run();
            }
        });
        //stop shift button
        pauseShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startTime = System.currentTimeMillis();
                run();
            }
        });

    }
}
