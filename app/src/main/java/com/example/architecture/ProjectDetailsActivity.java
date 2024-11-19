package com.example.architecture;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.architecture.Adapters.ImageSliderAdapter;
import com.example.architecture.R;
import com.example.architecture.models.Project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProjectDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ProjectDetailsActivity";

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView completionDateTextView;
    private TextView budgetTextView;
    private TextView projectTypeTextView;
    private TextView teamMembersTextView;
    private TextView contactEmailTextView;
    private ViewPager2 imagesViewPager;
    private ImageSliderAdapter imageSliderAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        // Initialize UI components
        nameTextView = findViewById(R.id.text_view_name);
        descriptionTextView = findViewById(R.id.text_view_description);
        locationTextView = findViewById(R.id.text_view_location);
        completionDateTextView = findViewById(R.id.text_view_completion_date);
        budgetTextView = findViewById(R.id.text_view_budget);
        projectTypeTextView = findViewById(R.id.text_view_project_type);
        teamMembersTextView = findViewById(R.id.text_view_team_members);
        contactEmailTextView = findViewById(R.id.text_view_contact_email);
        imagesViewPager = findViewById(R.id.view_pager_images);

        // Get project ID from the intent
        String projectId = getIntent().getStringExtra("projectId");
        if (projectId != null) {
            fetchProjectDetails(projectId);
        } else {
            Toast.makeText(this, "No Project ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchProjectDetails(String projectId) {
        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("projects").child(projectId);

        // Fetch project data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                if (project != null) {
                    displayProjectDetails(project);
                } else {
                    Toast.makeText(ProjectDetailsActivity.this, "Project not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching project details", databaseError.toException());
                Toast.makeText(ProjectDetailsActivity.this, "Error loading project", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProjectDetails(Project project) {
        nameTextView.setText(project.getName());
        descriptionTextView.setText(project.getDescription());
        locationTextView.setText("Location: " + project.getLocation());
        completionDateTextView.setText("Completion Date: " + project.getCompletionDate());
        budgetTextView.setText("Budget: $" + project.getBudgetEstimate());
        projectTypeTextView.setText("Type: " + project.getProjectType());
        teamMembersTextView.setText("Team: " + project.getFormattedTeamMembers());
        contactEmailTextView.setText("Contact: " + project.getContactEmail());

        // Set up image slider for project images
        List<String> imageUrls = project.getImages();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
            imagesViewPager.setAdapter(imageSliderAdapter);
        }
    }
}
