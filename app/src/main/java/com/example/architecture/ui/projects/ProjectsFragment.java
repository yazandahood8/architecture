package com.example.architecture.ui.projects;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architecture.Adapters.ProjectsAdapter;
import com.example.architecture.R;
import com.example.architecture.models.Project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProjectsAdapter projectsAdapter;
    private List<Project> projectList;
    private DatabaseReference databaseReference;

    private EditText searchName;
    private Spinner spinnerLocation;
    private String selectedCountry = "All"; // Default to "All" to show all countries initially
    private String queryName = ""; // Default to an empty search

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        // Initialize search fields
        searchName = view.findViewById(R.id.search_name);
        spinnerLocation = view.findViewById(R.id.spinner_location);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize project list and adapter
        projectList = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(getContext(), projectList);
        recyclerView.setAdapter(projectsAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("projects");

        // Set up country list in the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.country_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        // Set up listeners for search and spinner
        setupSearchListeners();

        // Fetch data from Firebase initially
        fetchProjectsFromFirebase();

        return view;
    }

    private void setupSearchListeners() {
        // Listen for text input changes in the search field
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryName = s.toString().toLowerCase(); // Update the query name and fetch data
                fetchProjectsFromFirebase();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Listen for item selection changes in the spinner
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = parent.getItemAtPosition(position).toString();
                fetchProjectsFromFirebase(); // Fetch data each time a new country is selected
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchProjectsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project project = snapshot.getValue(Project.class);
                    if (project != null) {
                        String projectName = project.getName() != null ? project.getName().toLowerCase() : "";
                        String projectLocation = project.getLocation() != null ? project.getLocation() : "";

                        // Split location to get the country part if format is "City, Country"
                        String projectCountry = "";
                        String[] locationParts = projectLocation.split(",\\s*");
                        if (locationParts.length > 1) {
                            projectCountry = locationParts[1]; // Extract country after the comma
                        }

                        // Filter by name and selected country
                        boolean matchesName = projectName.contains(queryName);
                        boolean matchesCountry = selectedCountry.equals("All") || projectCountry.equals(selectedCountry);

                        if (matchesName && matchesCountry) {
                            projectList.add(project); // Add project if it matches both filters
                        }
                    }
                }
                projectsAdapter.notifyDataSetChanged(); // Update the adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProjectsFragment", "Error fetching data", error.toException());
                Toast.makeText(getContext(), "Error loading projects", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
