package com.example.architecture.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architecture.R;
import com.example.architecture.models.Project;
import com.example.architecture.ProjectDetailsActivity;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {

    private final List<Project> projectList;
    private final Context context;

    public ProjectsAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);
        holder.bind(project);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView descriptionTextView;
        private final TextView locationTextView;
        private final TextView completionDateTextView;
        private final Button viewDetailsButton;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_project_name);
            descriptionTextView = itemView.findViewById(R.id.text_view_project_description);
            locationTextView = itemView.findViewById(R.id.text_view_project_location);
            completionDateTextView = itemView.findViewById(R.id.text_view_project_completion_date);
            viewDetailsButton = itemView.findViewById(R.id.button_view_details);
        }

        public void bind(Project project) {
            nameTextView.setText(project.getName());
            descriptionTextView.setText(project.getDescription());
            locationTextView.setText("Location: " + project.getLocation());
            completionDateTextView.setText(project.getCompletionDate());

            // Set click listener for the "View Details" button
            viewDetailsButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProjectDetailsActivity.class);
                intent.putExtra("projectId", project.getId()); // Pass project ID or other details as needed
                context.startActivity(intent);
            });
        }
    }
}