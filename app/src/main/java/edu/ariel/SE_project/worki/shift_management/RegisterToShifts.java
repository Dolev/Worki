package edu.ariel.SE_project.worki.shift_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.Shift;

public class RegisterToShifts extends AppCompatActivity implements View.OnClickListener
{
    private ListView shiftsView;
    private Button accept, reject;
    private List<Shift> shifts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_shifts);

        shiftsView = findViewById(R.id.shift_list);
        shiftsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);

        CurrentShifts.getInstance().addOnShiftsChangedListener(new Consumer<List<Shift>>()
        {
            @Override
            public void accept(List<Shift> shifts)
            {
                updateUI(shifts);
            }
        });

        accept.setOnClickListener(this);
        reject.setOnClickListener(this);
    }

    private void updateUI(List<Shift> shifts)
    {
        shiftsView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shifts));
        this.shifts = shifts;
    }

    @Override
    public void onClick(View v)
    {
        if (shifts == null)
            return;
        Shift selection = shifts.get(shiftsView.getCheckedItemPosition());
        if (v.equals(accept))
        {
            CurrentShifts.getInstance().registerToShift(selection, CurrentUser.getInstance().getUserData());
        } else if (v.equals(reject))
        {
            CurrentShifts.getInstance().unregisterToShift(selection, CurrentUser.getInstance().getUserData());
        }
    }
}
