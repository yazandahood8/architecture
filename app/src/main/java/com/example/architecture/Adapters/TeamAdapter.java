package com.example.architecture.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.architecture.R;
import com.example.architecture.models.TeamMember;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private final List<TeamMember> userList;
    private final Context context;
    private final OnRequestClickListener onRequestClickListener;

    public interface OnRequestClickListener {
        void onRequestClick(String userId);
    }

    public TeamAdapter(Context context, List<TeamMember> userList, OnRequestClickListener onRequestClickListener) {
        this.context = context;
        this.userList = userList;
        this.onRequestClickListener = onRequestClickListener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_team_member, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        TeamMember user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView roleTextView;
        private final ImageView profileImageView;
        private final Button requestButton;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_name);
            roleTextView = itemView.findViewById(R.id.text_view_role);
            profileImageView = itemView.findViewById(R.id.image_view_profile);
            requestButton = itemView.findViewById(R.id.button_request);
        }

        public void bind(TeamMember user) {
            nameTextView.setText(user.getName());
            roleTextView.setText(user.getEmail()); // Display email as a placeholder for role

            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .placeholder(R.drawable.profile)
                    .into(profileImageView);

            requestButton.setOnClickListener(v -> onRequestClickListener.onRequestClick(user.getEmail()));
        }
    }
}