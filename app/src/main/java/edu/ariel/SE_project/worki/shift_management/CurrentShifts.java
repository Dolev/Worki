package edu.ariel.SE_project.worki.shift_management;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
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
                DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + user.companyId);
                myRef.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Shift shift = new Shift().readFromDatabase(dataSnapshot);
                        shifts.put(dataSnapshot.getKey(), shift);
                        onChange();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Shift shift = new Shift().readFromDatabase(dataSnapshot);
                        shifts.put(dataSnapshot.getKey(), shift);
                        onChange();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {
                        Shift shift = new Shift().readFromDatabase(dataSnapshot);
                        shifts.remove(dataSnapshot.getKey());
                        onChange();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Log.e("CurrentShifts", "ChildEventListener canceled");
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

    public void registerToShift(Shift shift, User user)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + user.companyId);

        if (shifts.containsKey(shift.getShiftId()))
        {
            shift.addWorkerToShift(user.email);
            shift.writeToDatabase(myRef.child(shift.getShiftId()));
        } else
        {
            Log.w("CurrentShifts", "register: shift not in list.");
        }
    }

    public void unregisterToShift(Shift shift, User user)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + user.companyId);

        if (shifts.containsKey(shift.getShiftId()))
        {
            shift.removeWorkerFromShift(user.email);
            shift.writeToDatabase(myRef.child(shift.getShiftId()));
        } else
        {
            Log.w("CurrentShifts", "unregister: shift not in list.");
        }
    }

    public void addShift(Date start, Date end, User user, int numOfWorkers)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + user.companyId).push();
        Shift shift = new Shift(user.email, new HashMap<String, Long>(), myRef.getKey(), start, end, numOfWorkers, user.companyId);
        shift.writeToDatabase(myRef);
    }

    public void updateShift(Shift shift)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + shift.getCompanyId()).child(shift.getShiftId());
        shift.writeToDatabase(myRef);
    }

    public void removeShift(Shift shift, User user)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalMetaData.shiftsPath + '/' + user.companyId).child(shift.getShiftId());
        myRef.removeValue();
    }

    public Shift getCurrentShift()
    {
        Date current = new Date();
        Log.d("CurrentShift", "getCurrentShift. Current date: " + current + " shifts: " + getShifts());
        for (Shift s : getShifts())
        {
            if (s.getShiftDate().before(current) && s.getShiftEnd().after(current))
            {
                Log.d("CurrentShift", "getCurrentShift. current shift: " + s);
                return s;
            }
        }
        return null;
    }
}
