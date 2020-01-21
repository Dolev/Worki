package edu.ariel.SE_project.worki.data;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shift implements ReadableFromDatabase, WriteableToDatabase
{
    private String shiftManager;

    private Map<String, Long> workersInShift = new HashMap<>();
    private String shiftId;
    private Date shiftDate;
    private Date shiftEnd;
    private int numOfWorkers;
    private String companyId;

    public String getCompanyId()
    {
        return companyId;
    }

    public int getNumOfWorkers()
    {
        return numOfWorkers;
    }

    public String getShiftManager()
    {
        return shiftManager;
    }

    public List<String> getWorkersInShift()
    {
        return new ArrayList<>(workersInShift.keySet());
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
        this.companyId = shift.companyId;
    }

    public Shift(String shiftManager, Map<String, Long> workersInShift, String shiftId, Date shiftDate, Date shiftEnd, int numOfWorkers, String companyId)
    {
        this.shiftManager = shiftManager;
        this.shiftId = shiftId;
        this.workersInShift = workersInShift;
        this.shiftDate = shiftDate;
        this.shiftEnd = shiftEnd;
        this.numOfWorkers = numOfWorkers;
        this.companyId = companyId;
    }

    public void addWorkerToShift(String worker)
    {
        workersInShift.put(worker, 0L);
    }

    public void removeWorkerFromShift(String worker)
    {
        workersInShift.remove(worker);
    }

    public void setTime(String worker, Long time)
    {
        if (worker == null)
            return;
        workersInShift.put(worker, time);
    }

    public Long getTime(String worker)
    {
        return workersInShift.get(worker);
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
        Map<String, Long> shiftWorkers = (Map<String, Long>) snapshot.child("workersInShift").getValue();
        if (shiftWorkers == null)
            shiftWorkers = new HashMap<>();

        shiftWorkers = toAppFormat(shiftWorkers);

        Date shiftDate = snapshot.child("shiftDate").getValue(Date.class);
        Date shiftEnd = snapshot.child("shiftEnd").getValue(Date.class);
        Integer numOfWorkers = snapshot.child("numOfWorkers").getValue(Integer.class);
        String companyId = snapshot.child("companyId").getValue(String.class);

        if (numOfWorkers == null)
            numOfWorkers = 0;

        return new Shift(shiftManager, shiftWorkers, snapshot.getKey(), shiftDate, shiftEnd, numOfWorkers, companyId);
    }

    private Map<String, Long> toAppFormat(Map<String, Long> map)
    {
        Map<String, Long> _map = new HashMap<>();
        for (String email :
                map.keySet())
        {
            Long time = map.get(email);
            _map.put(email.replace("-at-", "@").replace("-dot-", "."), time);
        }
        return _map;
    }

    private Map<String, Long> toWebFormat(Map<String, Long> map)
    {
        Map<String, Long> _map = new HashMap<>();
        for (String email :
                map.keySet())
        {
            Long time = map.get(email);
            _map.put(email.replace("@", "-at-").replace(".", "-dot-"), time);
        }
        return _map;
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
        reference.child("workersInShift").setValue(toWebFormat(workersInShift));
        reference.child("shiftId").setValue(shiftId);
        reference.child("shiftDate").setValue(shiftDate);
        reference.child("shiftEnd").setValue(shiftEnd);
        reference.child("numOfWorkers").setValue(numOfWorkers);
        reference.child("companyId").setValue(companyId);

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shift shift = (Shift) o;

        if (numOfWorkers != shift.numOfWorkers) return false;
        if (!shiftManager.equals(shift.shiftManager)) return false;
        if (workersInShift != null ? !workersInShift.equals(shift.workersInShift) : shift.workersInShift != null)
            return false;
        if (!shiftId.equals(shift.shiftId)) return false;
        if (shiftDate != null ? !shiftDate.equals(shift.shiftDate) : shift.shiftDate != null)
            return false;
        if (shiftEnd != null ? !shiftEnd.equals(shift.shiftEnd) : shift.shiftEnd != null)
            return false;
        return companyId.equals(shift.companyId);
    }

    @Override
    public int hashCode()
    {
        int result = shiftManager.hashCode();
        result = 31 * result + (workersInShift != null ? workersInShift.hashCode() : 0);
        result = 31 * result + shiftId.hashCode();
        result = 31 * result + (shiftDate != null ? shiftDate.hashCode() : 0);
        result = 31 * result + (shiftEnd != null ? shiftEnd.hashCode() : 0);
        result = 31 * result + numOfWorkers;
        result = 31 * result + companyId.hashCode();
        return result;
    }
}
