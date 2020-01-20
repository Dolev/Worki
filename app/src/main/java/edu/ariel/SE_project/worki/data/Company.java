package edu.ariel.SE_project.worki.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * An object to store company data.
 */
public class Company implements ReadableFromDatabase, WriteableToDatabase
{
    public String id = "";
    public String name = "";
    public String address = "";
    public List<String> workers = new ArrayList<>();

    /**
     * Create an empty company object.
     */
    public Company()
    {

    }

    /**
     * Create a new company object.
     *
     * @param id      the company's Id.
     * @param name    the company's name.
     * @param address the company's address.
     */
    public Company(String id, String name, String address)
    {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Company(String id, String name, String address, List<String> workers)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.workers = workers;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id.equals(company.id);
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public Company readFromDatabase(DataSnapshot snapshot)
    {
        String id = snapshot.getKey();
        String name = snapshot.child("name").getValue(String.class);
        String address = snapshot.child("address").getValue(String.class);
        List<String> workers = snapshot.child("workers").getValue(ArrayList.class);
        return new Company(id, name, address, workers);
    }

    @Override
    public void writeToDatabase(DatabaseReference reference)
    {
        reference.child("name").setValue(name);
        reference.child("address").setValue(address);
        reference.child("workers").setValue(workers);
    }
}
