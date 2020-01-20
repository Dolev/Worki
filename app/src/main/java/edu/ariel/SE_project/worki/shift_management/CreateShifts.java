package edu.ariel.SE_project.worki.shift_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.Shift;
import edu.ariel.SE_project.worki.data.User;

public class CreateShifts extends AppCompatActivity
{
    private Button add, remove;
    private Calendar startCal = Calendar.getInstance(), endCal = Calendar.getInstance();
    private List<Shift> shifts = null;
    private ListView shiftsView;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shifts);

        add = findViewById(R.id.add);
        shiftsView = findViewById(R.id.shift_list);
        shiftsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        remove = findViewById(R.id.remove);

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                firstDialog.run();
            }
        });
        CurrentShifts.getInstance().addOnShiftsChangedListener(new Consumer<List<Shift>>()
        {
            @Override
            public void accept(List<Shift> shifts)
            {
                updateUI(shifts);
            }
        });

        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
                {
                    @Override
                    public void accept(User user)
                    {
                        if (shiftsView.getCheckedItemPosition() < 0)
                            return;
                        Shift selection = shifts.get(shiftsView.getCheckedItemPosition());
                        CurrentShifts.getInstance().removeShift(selection, user);
                    }
                });
            }
        });

    }

    private void updateUI(List<Shift> shifts)
    {
        shiftsView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shifts));
        this.shifts = shifts;
    }

    private void setDate(final Calendar c, String title, final Runnable next)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        next.run();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle(title);
        datePickerDialog.show();
    }

    private void setTime(final Calendar c, String title, final Runnable next)
    {

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener()
                {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute)
                    {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        next.run();
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.setTitle(title);
        timePickerDialog.show();
    }

    private void setCal(final Calendar cal, String dateTitle, final String timeTitle,
                        final Runnable next)
    {
        setDate(cal, dateTitle, new Runnable()
        {
            @Override
            public void run()
            {
                setTime(cal, timeTitle, next);
            }
        });
    }

    private Runnable firstDialog = new Runnable()
    {
        @Override
        public void run()
        {
            setCal(startCal, "Start Date", "Start Time", secondDialog);
        }
    };
    private Runnable secondDialog = new Runnable()
    {
        @Override
        public void run()
        {
            setCal(endCal, "End Date", "End Time", thirdDialog);
        }
    };

    private Runnable thirdDialog = new Runnable()
    {
        @Override
        public void run()
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateShifts.this);
            dialogBuilder.setTitle("Add New Shift?");
            DateFormat df = SimpleDateFormat.getDateTimeInstance();
            dialogBuilder.setMessage("Start: " + df.format(startCal.getTime()) + "\nEnd: " + df.format(endCal.getTime()));

            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    addShift();
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
                }
            });

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                }
            });


            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
    };

    private void addShift()
    {
        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {
                CurrentShifts.getInstance()
                        .addShift(startCal.getTime(), endCal.getTime(), CurrentUser.getInstance().getUserData());
            }
        });
    }
}
