package edu.ariel.SE_project.worki.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import edu.ariel.SE_project.worki.R;

public class TimerActivity extends AppCompatActivity
{
    /**
     * Update period (in milliseconds).
     */
    private static final int timerUpdatePeriod = 100;

    private Button startShift;
    private Button stopShift;
    private Button pauseShift;
    private TextView timer;
    private long time = 0;
    private long startTime = 0;
    private long lastTime = 0;
    private boolean paused = false;

    Timer timing = new Timer("MyTimer");//create a new TimerActivity

    //timer:
    final TimerTask timerTask = new TimerTask()
    {

        @Override
        public void run()
        {
            updateTimer();
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

        pauseShift = findViewById(R.id.pauseShift);

        timer = findViewById(R.id.timer);


        startShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startTimer();
            }
        });

        pauseShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pauseTimer();
            }
        });


    }

    /**
     * Start the timer.
     */
    private void startTimer()
    {
        lastTime = startTime = System.currentTimeMillis();
        timing.scheduleAtFixedRate(timerTask, timerUpdatePeriod, timerUpdatePeriod);
    }

    /**
     * Update the timer.
     */
    private void updateTimer()
    {
        if (!paused)
        {
            time += System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
        }
        timer.setText(String.format(Locale.getDefault(), "%tT", time));
    }

    /**
     * Pause the timer.
     */
    private void pauseTimer()
    {
        timing.cancel();
        updateTimer();
        paused = true;
    }

    /**
     * Continue (unpause) the timer.
     */
    private void continueTimer()
    {
        paused = false;
        lastTime = System.currentTimeMillis();
        timing.scheduleAtFixedRate(timerTask, timerUpdatePeriod, timerUpdatePeriod);
    }

    /**
     * Stop the timer.
     */
    private void stopTimer()
    {
        timing.cancel();
        time = 0;
    }
}
