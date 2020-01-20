package edu.ariel.SE_project.worki.assistance_classes;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ariel.SE_project.worki.R;
import edu.ariel.SE_project.worki.data.CurrentUser;
import edu.ariel.SE_project.worki.data.User;
import edu.ariel.SE_project.worki.shift_management.CreateShifts;
import edu.ariel.SE_project.worki.shift_management.RegisterToShifts;
import edu.ariel.SE_project.worki.signed_in_activities.ListOfUserConnected;
import edu.ariel.SE_project.worki.signed_in_activities.TimerActivity;
import edu.ariel.SE_project.worki.worker_to_company_registration.RegisterWorkerToCompanyActivity;
import edu.ariel.SE_project.worki.worker_to_company_registration.RegistrationOfWorkerFromCompaniesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment
{

    private List<MenuEntry> entries = new LinkedList<>();
    private List<MenuEntry> managerOnly = new LinkedList<>();
    private List<MenuEntry> workerOnly = new LinkedList<>();


    public MenuFragment()
    {
        // Add menu entries here

        entries.add(new MenuEntry("Timer", TimerActivity.class));
        entries.add(new MenuEntry("Show Current Users", ListOfUserConnected.class));

        managerOnly.add(new MenuEntry("Register Workers", RegisterWorkerToCompanyActivity.class));
        managerOnly.add(new MenuEntry("Manage Shifts", CreateShifts.class));

        workerOnly.add(new MenuEntry("Register to Company", RegistrationOfWorkerFromCompaniesActivity.class));
        workerOnly.add(new MenuEntry("Shifts", RegisterToShifts.class));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meun, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        if (getActivity() != null)
        {
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra("isManager"))
            {
                isManager = intent.getBooleanExtra("isManager", false);
            }

            Log.d("Menu", "@onActivityCreated: hasExtra: " + intent.hasExtra("isManager") + ", isManager: " + isManager);

            if (menuCreated)
            {
                getActivity().invalidateOptionsMenu();
            }
        }
    }


    private Boolean isManager = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);


        Log.d("Menu", "@onCreate");

        setHasOptionsMenu(true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null)
        {
            if (getActivity() != null)
                Transitions.toMainActivity(getActivity());
        }
    }


    private class MenuEntry
    {
        String name;
        Class cls;
        MenuItem item;

        MenuEntry(String name, Class cls)
        {
            this.name = name;
            this.cls = cls;
        }
    }


    private boolean menuCreated = false;

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater)
    {

        Log.d("Menu", "@onCreateOptionsMenu: menuCreated: " + menuCreated + ", isManager: " + isManager);

        for (MenuEntry entry : entries)
        {
            entry.item = menu.add(entry.name);
        }
        if (isManager != null)
        {
            if (isManager)
            {
                for (MenuEntry entry : managerOnly)
                {
                    entry.item = menu.add(entry.name);
                }
            } else
            {
                for (MenuEntry entry : workerOnly)
                {
                    entry.item = menu.add(entry.name);
                }
            }
        }


        menuCreated = true;

        super.onCreateOptionsMenu(menu, inflater);

    }

    // This method allows the user to navigate on the app through the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent intent = null;
        for (MenuEntry entry : entries)
        {
            if (item == entry.item)
            {
                intent = new Intent(getActivity(), entry.cls);
            }
        }

        for (MenuEntry entry : managerOnly)
        {
            if (item == entry.item)
            {
                intent = new Intent(getActivity(), entry.cls);
            }
        }

        for (MenuEntry entry : workerOnly)
        {
            if (item == entry.item)
            {
                intent = new Intent(getActivity(), entry.cls);
            }
        }

        if (intent != null)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
