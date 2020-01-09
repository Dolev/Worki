package edu.ariel.SE_project.worki.shift_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.ariel.SE_project.worki.assistance_classes.GlobalMetaData;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.Shift;
import edu.ariel.SE_project.worki.data.User;

public class CurrentShifts
{
    private static CurrentShifts instance = new CurrentShifts();

    private HashMap<String, Shift> shifts = new HashMap<>();

    private List<Consumer<List<Shift>>> listeners = new LinkedList<>();

    public static CurrentShifts getInstance()
    {
        return instance;
    }

    private CurrentShifts()
    {
        CurrentUser.getInstance().addOnUserNotNullListener(new Consumer<User>()
        {
            @Override
            public void accept(User user)
            {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + user.id);
                myRef.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Shift shift = new Shift().readFromDatabase(dataSnapshot);
                        shifts.put(shift.shiftManager.id, shift);
                        onChange();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Shift shift = new Shift().readFromDatabase(dataSnapshot);
                        shifts.put(shift.shiftManager.id, shift);
                        onChange();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {
                        Shift shift = new Shift().readFromDatabase(dataSnapshot);
                        shifts.remove(shift.shiftManager.id);
                        onChange();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
        });
    }

    public List<Shift> getShifts()
    {
        return new ArrayList<>(shifts.values());
    }

    public void addOnShiftsChangedListener(Consumer<List<Shift>> listener)
    {
        listeners.add(listener);
    }

    private void onChange()
    {
        List<Shift> shifts = getShifts();
        for (Consumer<List<Shift>> listener : listeners)
        {
            listener.accept(shifts);
        }
    }

}
