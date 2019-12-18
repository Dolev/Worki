package edu.ariel.SE_project.worki.data;

public class Company
{
    public int companyId = 0;
    public String companyName = "";
    public String address = "";

    public Company()
    {

    }

    public Company(int companyId, String companyName, String address)
    {
        this.companyId = companyId;
        this.companyName = companyName;
        this.address = address;
    }
}
