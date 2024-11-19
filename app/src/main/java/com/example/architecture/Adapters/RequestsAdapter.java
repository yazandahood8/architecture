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
import com.example.architecture.models.Requester;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private final List<Requester> requestList;
    private final Context context;
    private final OnRequestActionListener requestActionListener;

    public interface OnRequestActionListener {
        void onRequestAction(String requestId, boolean accept,String email);
    }

    public RequestsAdapter(Context context, List<Requester> requestList, OnRequestActionListener listener) {
        this.context = context;
        this.requestList = requestList;
        this.requestActionListener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Requester requester = requestList.get(position);
        holder.bind(requester);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView emailTextView;
        private final ImageView profileImageView;
        private final Button acceptButton;
        private final Button rejectButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_name);
            emailTextView = itemView.findViewById(R.id.text_view_email);
            profileImageView = itemView.findViewById(R.id.image_view_profile);
            acceptButton = itemView.findViewById(R.id.button_accept);
            rejectButton = itemView.findViewById(R.id.button_reject);
        }

        public void bind(Requester requester) {
            loadProfileData(requester.getRequestId().replace(".",","));
//            nameTextView.setText(requester.getRequesterId()); // Display requester ID or name if available
//            emailTextView.setText(requester.getRecipientId()); // Display recipient ID for reference
//
//            // Assuming the profile image URL is part of the requester object
//            Glide.with(context)
//                    .load(requester.getProfileImageUrl())
//                    .placeholder(R.drawable.profile) // Replace with an actual placeholder image if available
//                    .into(profileImageView);

            // Set up button click listeners for accept and reject
            acceptButton.setOnClickListener(v -> requestActionListener.onRequestAction(requester.getRequestId(), true,requester.getRequesterId()));
            rejectButton.setOnClickListener(v -> requestActionListener.onRequestAction(requester.getRequestId(), false,requester.getRequesterId()));
        }
        private void loadProfileData(String email) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(email);

            // Fetch user data from Firebase
            userRef.get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    String name=dataSnapshot.child("name").getValue(String.class);
                    String role=dataSnapshot.child("role").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    nameTextView.setText(name);
                    emailTextView.setText(role);
                    Glide.with(context).load(profileImageUrl).placeholder(R.drawable.profile).into(profileImageView);
                }
            });
        }
    }


}
