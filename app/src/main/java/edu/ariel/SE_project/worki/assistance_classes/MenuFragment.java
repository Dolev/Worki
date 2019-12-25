package edu.ariel.SE_project.worki.assistance_classes;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import edu.ariel.SE_project.worki.TimerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment
{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meun, container, false);
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
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

    private List<MenuEntry> entries = new LinkedList<>();


    public MenuFragment()
    {
        // Add menu entries here

        entries.add(new MenuEntry("Timer", TimerActivity.class));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        // TODO don't show current activity in menu (doesn't work)
        for (MenuEntry entry : entries)
        {
            if ((getActivity() == null) || (entry.cls.equals(getActivity().getClass())))
            {
                entry.item = menu.add(entry.name);
            }
        }
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

        if (intent != null)
            startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
