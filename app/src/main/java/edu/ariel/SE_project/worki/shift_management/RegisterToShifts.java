package edu.ariel.SE_project.worki.shift_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.Shift;

public class RegisterToShifts extends AppCompatActivity
{
    ListView shiftsView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_shifts);

        shiftsView = findViewById(R.id.shift_list);

        CurrentShifts.getInstance().addOnShiftsChangedListener(new Consumer<List<Shift>>()
        {
            @Override
            public void accept(List<Shift> shifts)
            {
                updateUI(shifts);
            }
        });
    }

    private void updateUI(List<Shift> shifts)
    {
        shiftsView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shifts));
    }
}
