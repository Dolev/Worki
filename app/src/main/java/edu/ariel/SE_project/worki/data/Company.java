package edu.ariel.SE_project.worki.data;

import java.util.ArrayList;
import java.util.List;

/**
 * An object to store company data.
 */
public class Company
{
    public int companyId = 0;
    public String companyName = "";
    public String address = "";
    public List<User> workers = new ArrayList<>();

    /**
     * Create an empty company object.
     */
    public Company()
    {

    }

    /**
     * Create a new company object.
     *
     * @param companyId   the company's Id.
     * @param companyName the company's nmae.
     * @param address     the company's address.
     */
    public Company(int companyId, String companyName, String address)
    {
        this.companyId = companyId;
        this.companyName = companyName;
        this.address = address;
    }


}
