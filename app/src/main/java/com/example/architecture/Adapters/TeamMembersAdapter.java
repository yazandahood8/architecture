package com.example.architecture.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.architecture.R;
import com.example.architecture.models.TeamMember;

import java.util.List;

public class TeamMembersAdapter extends RecyclerView.Adapter<TeamMembersAdapter.TeamMemberViewHolder> {

    private final List<TeamMember> teamMembers;
    private final Context context;

    public TeamMembersAdapter(Context context, List<TeamMember> teamMembers) {
        this.context = context;
        this.teamMembers = teamMembers;
    }

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_team_member, parent, false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        TeamMember teamMember = teamMembers.get(position);
        holder.bind(teamMember);
    }

    @Override
    public int getItemCount() {
        return teamMembers.size();
    }

    class TeamMemberViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView profileImageView;
        private final TextView text_view_role;


        public TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_name);
            profileImageView = itemView.findViewById(R.id.image_view_profile);
            text_view_role = itemView.findViewById(R.id.text_view_role);

        }

        public void bind(TeamMember teamMember) {
            nameTextView.setText(teamMember.getName());
            text_view_role.setText(teamMember.getRole());
            Glide.with(context)
                    .load(teamMember.getProfileImageUrl())
                    .placeholder(R.drawable.profile)
                    .into(profileImageView);
        }
    }
}
