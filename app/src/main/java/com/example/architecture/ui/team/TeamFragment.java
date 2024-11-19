package com.example.architecture.ui.team;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architecture.Adapters.RequestsAdapter;
import com.example.architecture.Adapters.TeamAdapter;
import com.example.architecture.Adapters.TeamMembersAdapter;
import com.example.architecture.R;
import com.example.architecture.models.Requester;
import com.example.architecture.models.TeamMember;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamFragment extends Fragment {

    private RecyclerView recyclerView, recyclerViewRequests, recyclerViewMyTeam;
    private TeamAdapter teamAdapter;
    private RequestsAdapter requestsAdapter;
    private TeamMembersAdapter teamMembersAdapter;
    private List<TeamMember> userList;
    private List<Requester> requestList;
    private List<TeamMember> teamMembersList;
    private EditText searchInput;
    private String currentUserEmail;
    private String myTeamId=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            currentUserEmail = currentUser.getEmail();
        } else {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }

        recyclerView = view.findViewById(R.id.recycler_view_team);
        recyclerViewRequests = view.findViewById(R.id.recycler_view_requests);
        recyclerViewMyTeam = view.findViewById(R.id.recycler_view_my_team);
        searchInput = view.findViewById(R.id.search_input);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyTeam.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        requestList = new ArrayList<>();
        teamMembersList = new ArrayList<>();

        teamAdapter = new TeamAdapter(getContext(), userList, this::sendTeamRequest);
        requestsAdapter = new RequestsAdapter(getContext(), requestList, this::handleRequest);
        teamMembersAdapter = new TeamMembersAdapter(getContext(), teamMembersList);

        recyclerView.setAdapter(teamAdapter);
        recyclerViewRequests.setAdapter(requestsAdapter);
        recyclerViewMyTeam.setAdapter(teamMembersAdapter);
        getMyTeam();
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchUsers(s.toString());
                } else {
                    userList.clear();
                    teamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        fetchIncomingRequests();
        fetchTeamMembers();

        return view;
    }

    private void searchUsers(String query) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        String lowercaseQuery = query.toLowerCase(); // Convert the query to lowercase

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TeamMember user = snapshot.getValue(TeamMember.class);
                    if (user != null && !user.getEmail().equals(currentUserEmail)) {
                        // Compare the lowercase version of the user's name and the query
                        String userName = user.getName() != null ? user.getName().toLowerCase() : "";
                        if (userName.startsWith(lowercaseQuery)) {
                            userList.add(user);
                        }
                    }
                }
                teamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching search results", databaseError.toException());
                Toast.makeText(getContext(), "Error fetching users", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendTeamRequest(String recipientId) {
        DatabaseReference teamRequestsRef = FirebaseDatabase.getInstance().getReference("Users").child(recipientId.replace(".",",")).child("team_requests");



        DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("Users").child(recipientId.replace(".", ","));
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), recipientId+" ", Toast.LENGTH_SHORT).show();
                String team = dataSnapshot.child("team").getValue(String.class);
                if (team == null) {
                    String requestId = currentUserEmail.replace(".",",");
                    Map<String, Object> request = new HashMap<>();
                    request.put("requestId", requestId);

                    request.put("requesterId", currentUserEmail);
                    request.put("recipientId", recipientId);
                    request.put("status", "pending");
                    request.put("timestamp", "1");

                    teamRequestsRef.child(requestId).setValue(request)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Request sent successfully!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Log.e("Firebase", "Error sending request", e));
                } else {
                    Snackbar.make(searchInput, "User already in team", Snackbar.LENGTH_LONG).show();




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching team members", databaseError.toException());
            }
        });





    }

    private void fetchIncomingRequests() {
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".",",")).child("team_requests");
        requestsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Requester requester = snapshot.getValue(Requester.class);

                            if (requester != null && "pending".equals(requester.getStatus())) {

                                requestList.add(requester);
                            }
                        }
                        requestsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching incoming requests", databaseError.toException());
                    }
                });
    }

    private void handleRequest(String requestId, boolean accept,String email) {
        Toast.makeText(getContext(), requestId+"  ", Toast.LENGTH_SHORT).show();

//        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".",",")).child("team_requests").child(requestId);
        String newStatus = accept ? "accepted" : "rejected";
//        requestRef.child("status").setValue(newStatus).addOnSuccessListener(aVoid -> {
//            if (accept)
//                addMemberToTeam(email.replace(".",","));
//        });

        if(accept) {

            DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".", ","));
            teamRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String team = dataSnapshot.child("team").getValue(String.class);
                    if (team != null) {

                    } else {
                        Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                        DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".", ","));
                        String id = teamRef.push().getKey().toString();
                        team=id;
                        teamRef.child("team").setValue(id);


                        DatabaseReference teamRef2 = FirebaseDatabase.getInstance().getReference("Users").child(email.replace(".", ","));
                        teamRef2.child("team").setValue(id);

                    }
                    DatabaseReference teamRef3 = FirebaseDatabase.getInstance().getReference("teams").child(team).child(currentUserEmail.replace(".", ","));
                    teamRef3.setValue(true);

                    DatabaseReference teamRef4 = FirebaseDatabase.getInstance().getReference("teams").child(team).child(email.replace(".", ","));
                    teamRef4.setValue(true);

                    FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".", ",")).child("team_requests").child(email.replace(".",",")).removeValue();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching team members", databaseError.toException());
                }
            });

        }



    }



    private void fetchTeamMembers() {
        DatabaseReference teamRef4 = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".", ","));
        teamRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTeamId = dataSnapshot.child("team").getValue(String.class);
                if(myTeamId!=null) {
                    DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("teams").child(myTeamId);
                    teamRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String st = snapshot.getKey();
                                if (st != null) {

                                    DatabaseReference teamRef2 = FirebaseDatabase.getInstance().getReference("Users").child(st);
                                    teamRef2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // TeamMember member = snapshot.getValue(TeamMember.class);
                                                TeamMember member = new TeamMember();
                                                member.setName(dataSnapshot.child("name").getValue(String.class));
                                                member.setEmail(dataSnapshot.child("email").getValue(String.class));
                                                member.setPhone(dataSnapshot.child("phone").getValue(String.class));
                                                member.setPassword(dataSnapshot.child("password").getValue(String.class));
                                                member.setRole(dataSnapshot.child("role").getValue(String.class));
                                                member.setProfileImageUrl(dataSnapshot.child("profileImageUrl").getValue(String.class));


                                                if(!member.getEmail().equals(currentUserEmail))
                                                    teamMembersList.add(member);

                                            teamMembersAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("Firebase", "Error fetching team members", databaseError.toException());
                                        }
                                    });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Firebase", "Error fetching team members", databaseError.toException());
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching team members", databaseError.toException());
            }
        });




    }

    private void addMemberToTeam(String email) {
        // Replace "." with "," in email for Firebase key compatibility
        String emailKey = email.replace(".", ",");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(emailKey);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the user data from "users/{email}"
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();

                    // Save this data under "teams/{currentUserEmail}/members/{email}"
                    DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".", ","))
                            .child("teams")
                            .child("members")
                            .child(emailKey);

                    teamRef.setValue(userMap)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(getContext(), "User added to team successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Failed to add user to team", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(getContext(), "User not found in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMyTeam(){
        DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserEmail.replace(".", ","));
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 myTeamId = dataSnapshot.child("team").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching team members", databaseError.toException());
            }
        });
    }

}
