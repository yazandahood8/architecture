package com.example.architecture.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architecture.R;
import com.example.architecture.models.Material;

import java.util.List;

public class MaterialsAdapter extends RecyclerView.Adapter<MaterialsAdapter.MaterialViewHolder> {

    private final List<Material> materialsList;

    public MaterialsAdapter(List<Material> materialsList) {
        this.materialsList = materialsList;
    }

    @NonNull
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_item, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material material = materialsList.get(position);
        holder.nameTextView.setText(material.getName());
        holder.descriptionTextView.setText(material.getDescription());
        holder.typeTextView.setText(material.getType());
        holder.quantityTextView.setText("Quantity: " + material.getQuantity());
        holder.priceTextView.setText("Price: $" + material.getPrice());
    }

    @Override
    public int getItemCount() {
        return materialsList.size();
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, typeTextView, quantityTextView, priceTextView;

        MaterialViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.materialName);
            descriptionTextView = itemView.findViewById(R.id.materialDescription);
            typeTextView = itemView.findViewById(R.id.materialType);
            quantityTextView = itemView.findViewById(R.id.materialQuantity);
            priceTextView = itemView.findViewById(R.id.materialPrice);
        }
    }
}
