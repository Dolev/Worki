package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DataSnapshot;

public interface ReadableFromDatabase
{
    /**
     * Read a object from the database.
     *
     * @param snapshot the database snapshot where the object is stored.
     */
    ReadableFromDatabase readFromDatabase(DataSnapshot snapshot);
}
