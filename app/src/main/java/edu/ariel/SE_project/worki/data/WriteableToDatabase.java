package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DatabaseReference;

interface WriteableToDatabase
{
    void writeToDatabase(DatabaseReference reference);
}
