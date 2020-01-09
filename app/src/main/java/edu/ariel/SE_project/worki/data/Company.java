package edu.ariel.SE_project.worki.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An object to store company data.
 */
public class Company
{
    public String companyId = "";
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
     * @param companyName the company's name.
     * @param address     the company's address.
     */
    public Company(String companyId, String companyName, String address)
    {
        this.companyId = companyId;
        this.companyName = companyName;
        this.address = address;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return companyId.equals(company.companyId);
    }

    @Override
    public int hashCode()
    {
        return companyId.hashCode();
    }
}
