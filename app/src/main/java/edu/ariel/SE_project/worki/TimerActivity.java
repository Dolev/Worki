package edu.ariel.SE_project.worki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.assistance_classes.GlobalMetaData;
import edu.ariel.SE_project.worki.data.ShiftStamp;

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
                Toast.makeText(TimerActivity.this, "Have a nice day.",
                        Toast.LENGTH_SHORT).show();
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
        stopShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopTimer();
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
        writeDB(ShiftStamp.ShiftStampType.Start,startTime);
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
        pauseShift.setText("Continue Shift");
        writeDB(ShiftStamp.ShiftStampType.Pause,System.currentTimeMillis());
    }

    /**
     * Continue (unpause) the timer.
     */
    private void continueTimer()
    {
        paused = false;
        if (pauseShift.getText() == "Continue Shift")          //change button
        {
            pauseShift.setText("Pause Shift");
        }
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
        Toast.makeText(TimerActivity.this, "Come back soon!.",
                Toast.LENGTH_SHORT).show();
        writeDB(ShiftStamp.ShiftStampType.Stop,System.currentTimeMillis());
    }

    private void writeDB(ShiftStamp.ShiftStampType type,long time)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalMetaData.userDataPath());
        ShiftStamp ss = new ShiftStamp(type,time);
        ss.writeToDatabase(myRef.child("shiftStamps").child(ss.hashCode() + ""));
    }
}
