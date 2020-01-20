package edu.ariel.SE_project.worki.data;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shift implements ReadableFromDatabase, WriteableToDatabase
{
    private String shiftManager;

    private List<User> workersInShift = new ArrayList<>();
    private String shiftId;
    private Date shiftDate;
    private Date shiftEnd;
    private int numOfWorkers;

    public int getNumOfWorkers()
    {
        return numOfWorkers;
    }

    public String getShiftManager()
    {
        return shiftManager;
    }

    public List<User> getWorkersInShift()
    {
        return workersInShift;
    }

    public String getShiftId()
    {
        return shiftId;
    }

    public Date getShiftDate()
    {
        return shiftDate;
    }

    public Date getShiftEnd()
    {
        return shiftEnd;
    }

    public Shift()
    {

    }

    public Shift(Shift shift)
    {
        this.shiftManager = shift.shiftManager;
        this.shiftId = shift.shiftId;
        this.workersInShift = shift.workersInShift;
        this.shiftDate = shift.shiftDate;
        this.shiftEnd = shift.shiftEnd;
        this.numOfWorkers = shift.numOfWorkers;
    }

    public Shift(String shiftManager, List<User> workersInShift, String shiftId, Date shiftDate, Date shiftEnd, int numOfWorkers)
    {
        this.shiftManager = shiftManager;
        this.shiftId = shiftId;
        this.workersInShift = workersInShift;
        this.shiftDate = shiftDate;
        this.shiftEnd = shiftEnd;
        this.numOfWorkers = numOfWorkers;
    }

    public void addWorkerToShift(User worker)
    {
        workersInShift.add(worker);
    }

    public void removeWorkerFromShift(User worker)
    {
        workersInShift.remove(worker);
    }

    @NonNull
    public String toString()
    {
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        if (shiftDate == null || shiftEnd == null)
            return "";

        return "Start: " + df.format(shiftDate) + "\nEnd: " + df.format(shiftEnd) + "\nTotal: " + numOfWorkers
                + (workersInShift == null ? "" : ", Current: " + workersInShift.size());
    }

    /**
     * Read a object from the database.
     *
     * @param snapshot the database snapshot where the object is stored.
     */
    @Override
    public Shift readFromDatabase(DataSnapshot snapshot)
    {
        String shiftManager = snapshot.child("shiftManager").getValue(String.class);
        List<User> shiftWorkers = (List<User>) snapshot.child("workersInShift").getValue();
        if (shiftWorkers == null)
            shiftWorkers = new ArrayList<>();
        Date shiftDate = snapshot.child("shiftDate").getValue(Date.class);
        Date shiftEnd = snapshot.child("shiftEnd").getValue(Date.class);
        int numOfWorkers = snapshot.child("numOfWorkers").getValue(Integer.class);

        return new Shift(shiftManager, shiftWorkers, snapshot.getKey(), shiftDate, shiftEnd, numOfWorkers);
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
        reference.child("numOfWorkers").setValue(numOfWorkers);

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shift shift = (Shift) o;

        if (numOfWorkers != shift.numOfWorkers) return false;
        if (!shiftManager.equals(shift.shiftManager)) return false;
        if (!workersInShift.equals(shift.workersInShift)) return false;
        if (shiftId != null ? !shiftId.equals(shift.shiftId) : shift.shiftId != null) return false;
        if (!shiftDate.equals(shift.shiftDate)) return false;
        return shiftEnd.equals(shift.shiftEnd);
    }

    @Override
    public int hashCode()
    {
        int result = shiftManager.hashCode();
        result = 31 * result + workersInShift.hashCode();
        result = 31 * result + (shiftId != null ? shiftId.hashCode() : 0);
        result = 31 * result + shiftDate.hashCode();
        result = 31 * result + shiftEnd.hashCode();
        result = 31 * result + numOfWorkers;
        return result;
    }
}
