package com.example.mhike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.model.Hike;

import java.util.List;

// RecyclerView Adapter for displaying Hike items in a list
public class HikeAdapter
        extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    // Interface to handle user actions on individual hike items
    public interface OnHikeActionListener {
        void onEdit(Hike hike);
        void onDelete(Hike hike);
        void onObservations(Hike hike);
    }

    private List<Hike> hikes;
    private OnHikeActionListener listener;

    public HikeAdapter(List<Hike> hikes, OnHikeActionListener listener) {
        this.hikes = hikes;
        this.listener = listener;
    }

    // Refresh the adapter data when a search or update occurs
    public void updateList(List<Hike> newHikes) {
        this.hikes = newHikes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for a single hike item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikes.get(position);

        holder.tvHikeName.setText(hike.getName());
        String details = "Location: " + hike.getLocation() +
                        "\nDate: " + hike.getDate() +
                        "\nParking: " + hike.getParking() +
                        "\nLength: " + hike.getLength() + " km" +
                        "\nDifficulty: " + hike.getDifficulty();

        holder.tvHikeDetails.setText(details);

        // Map button clicks to listener interface
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(hike));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(hike));
        holder.btnObservations.setOnClickListener(v -> listener.onObservations(hike));
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    // ViewHolder class to cache UI references for performance
    static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView tvHikeName, tvHikeDetails;
        Button btnEdit, btnDelete, btnObservations;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvHikeDetails = itemView.findViewById(R.id.tvHikeDetails);
            btnEdit = itemView.findViewById(R.id.btnEditHike);
            btnDelete = itemView.findViewById(R.id.btnDeleteHike);
            btnObservations = itemView.findViewById(R.id.btnObservations);
        }
    }
}