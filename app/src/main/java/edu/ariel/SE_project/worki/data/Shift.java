package edu.ariel.SE_project.worki.data;

import androidx.annotation.RequiresPermission;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;

public class Shift implements ReadableFromDatabase,WriteableToDatabase
{
    private static int shiftIdGenerator = 0;
    private User shiftManager;
    private ArrayList<User> workersInShift;
    private int shiftId;
    private Date shiftDate;
    private Date shiftEnd;

    public Shift()
    {

    }

    public Shift (User shiftManager, int shiftLength)
    {
        this.shiftManager = shiftManager;
        this.shiftId = shiftIdGenerator++;
        shiftDate = new Date();
        shiftEnd = new Date(shiftDate.getTime() + shiftLength);

    }

    public Shift (Shift shift)
    {
        this.shiftManager = shift.shiftManager;
        this.shiftId = shift.shiftId;
        this.workersInShift = shift.workersInShift;
        this.shiftDate = shift.shiftDate;
        this.shiftEnd = shift.shiftEnd;
    }

    public Shift(User shiftManager, ArrayList<User> workersInShifts, int shiftId, Date shiftDate, Date shiftEnd)
    {
        this.shiftManager = shiftManager;
        this.shiftId = shiftId;
        this.workersInShift = workersInShift;
        this.shiftDate = shiftDate;
        this.shiftEnd = shiftEnd;
    }

    public void addWorkerToShift(User worker)
    {
        workersInShift.add(worker);
    }

    public void removeWorkerFromShift(User worker)
    {
        workersInShift.remove(worker);
    }

    public String toString()
    {
        String workers = workersInShift.toString();

        return "Shift {Shift Id: " + shiftId + '\'' +
                ", Manager Id: " + shiftManager.id + '\'' +
                ", Worker on Shift" + workers + '\'' +
                ", Shift Starts in: " + shiftDate.toString() +
                ", Shift Ends in: " + shiftEnd.toString() + '\'' +
                '}';
    }

    /**
     * Read a object from the database.
     *
     * @param snapshot the database snapshot where the object is stored.
     */
    @Override
    public Shift readFromDatabase(DataSnapshot snapshot)
    {
        User shiftManager = snapshot.child("shiftManager").getValue(User.class);
        ArrayList<User> shiftWorkers = snapshot.child("workersInShift").getValue(ArrayList.class);
        int shiftId = snapshot.child("shiftId").getValue(Integer.class);
        Date shiftDate = snapshot.child("shiftDate").getValue(Date.class);
        Date shiftEnd = snapshot.child("shiftEnd").getValue(Date.class);

        return new Shift(shiftManager, shiftWorkers, shiftId, shiftDate, shiftEnd);
    }

    /**
     * Store this Shift in the database.
     *
     * @param reference the database reference where the User would be stored.
     */
    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        reference.child("shiftManager").setValue(shiftManager);
        reference.child("workersInShift").setValue(workersInShift);
        reference.child("shiftId").setValue(shiftId);
        reference.child("shiftDate").setValue(shiftDate);
        reference.child("shiftEnd").setValue(shiftEnd);

    }
}
