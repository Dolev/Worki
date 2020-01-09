package edu.ariel.SE_project.worki.shift_management;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import edu.ariel.SE_project.worki.R;

public class CreateShifts extends AppCompatActivity implements View.OnClickListener
{
    TextView startDate, startTime, endDate, endTime;
    Calendar startCal = Calendar.getInstance(), endCal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shifts);

        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);
        endDate = findViewById(R.id.endDate);
        endTime = findViewById(R.id.endTime);

        startDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endDate.setOnClickListener(this);
        endTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.equals(startDate))
        {
            setDate(startCal, startDate, "Start Date: ");
        } else if (v.equals(startTime))
        {
            setTime(startCal, startTime, "Start Time: ");
        } else if (v.equals(endDate))
        {
            setDate(endCal, endDate, "End Date: ");
        } else if (v.equals(endTime))
        {
            setTime(endCal, endTime, "End Time: ");
        }
    }

    private void setDate(final Calendar c, final TextView text, final String prefix)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {

                        text.setText(prefix + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setTime(final Calendar c, final TextView text, final String prefix)
    {

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener()
                {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute)
                    {

                        text.setText(prefix + hourOfDay + ":" + minute);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }
}
