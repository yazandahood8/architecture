package com.example.architecture.ui.materials;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.architecture.Adapters.MaterialsAdapter;
import com.example.architecture.R;
import com.example.architecture.models.Material;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MaterialsFragment extends Fragment {

    private RecyclerView materialsRecyclerView;
    private MaterialsAdapter adapter;
    private List<Material> materialsList = new ArrayList<>();
    private DatabaseReference databaseReference;

    public MaterialsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_materials, container, false);

        // Initialize RecyclerView
        materialsRecyclerView = view.findViewById(R.id.materialsRecyclerView);
        materialsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MaterialsAdapter(materialsList);
        materialsRecyclerView.setAdapter(adapter);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("materials");

        // Fetch materials from Firebase Realtime Database
        fetchMaterialsFromDatabase();

        return view;
    }

    private void fetchMaterialsFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                materialsList.clear();
                for (DataSnapshot materialSnapshot : snapshot.getChildren()) {
                    // Get the unique id of each material
                    String id = materialSnapshot.getKey();

                    // Parse each snapshot into a Material object
                    String name = materialSnapshot.child("name").getValue(String.class);
                    String description = materialSnapshot.child("description").getValue(String.class);
                    String type = materialSnapshot.child("type").getValue(String.class);
                    Long quantity = materialSnapshot.child("quantity").getValue(Long.class);
                    Double price = materialSnapshot.child("price").getValue(Double.class);

                    if (id != null && name != null && description != null && type != null && quantity != null && price != null) {
                        Material material = new Material(id, name, description, type, quantity.intValue(), price);
                        materialsList.add(material);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
