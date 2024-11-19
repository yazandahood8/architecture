package com.example.architecture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.architecture.ui.projects.ProjectsFragment;
import com.example.architecture.ui.materials.MaterialsFragment;
import com.example.architecture.ui.team.TeamFragment;
import com.example.architecture.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Set default fragment when the app starts
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProjectsFragment())
                    .commit();
        }

        // Set BottomNavigationView item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            int id = item.getItemId();

                if(id== R.id.menu_projects) {
                    selectedFragment = new ProjectsFragment();
                }
            if(id== R.id.menu_materials) {
                selectedFragment = new MaterialsFragment();
            }
            if(id== R.id.menu_team) {
                selectedFragment = new TeamFragment();
            }
            if(id== R.id.menu_profile) {
                selectedFragment = new ProfileFragment();
            }
            if(id== R.id.menu_logout) {
                // Handle logout separately
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            // Replace fragment if selectedFragment is not null
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
