package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Class for saving shift start / end.
 */
public class ShiftStamp implements WriteableToDatabase, ReadableFromDatabase
{

    /**
     * Types of stamps.
     */
    public enum ShiftStampType
    {
        Start, Pause, Continue, Stop
    }

    /**
     * The type of this stamp.
     */
    public final ShiftStampType type;
    /**
     * The time of this stamp.
     */
    public final long utcTime;


    /**
     * Create a new ShiftStamp.
     *
     * @param type    the type of the stamp.
     * @param utcTime the time of the stamp.
     */
    public ShiftStamp(ShiftStampType type, long utcTime)
    {
        this.type = type;
        this.utcTime = utcTime;
    }

    /**
     * Read a ShiftStamp from the database.
     *
     * @param snapshot the database snapshot where the snapshot is stored.
     */
    public ShiftStamp(DataSnapshot snapshot)
    {
        type = snapshot.child("type").getValue(ShiftStampType.class);

        Long utcTimeL = snapshot.child("utcTime").getValue(Long.class);

        if (utcTimeL != null)
            utcTime = utcTimeL;
        else
            utcTime = 0;
    }

    @Override
    public ShiftStamp readFromDatabase(DataSnapshot snapshot)
    {
        return new ShiftStamp(snapshot);
    }

    /**
     * Write this ShiftStamp to the database.
     *
     * @param reference the database reference where the snapshot would be stored.
     */
    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        reference.child("type").setValue(type);
        reference.child("utcTime").setValue(utcTime);
    }
}
