package com.example.mhike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.model.Observation;

import java.util.List;

// RecyclerView Adapter for displaying Observation items in a list
public class ObservationAdapter
        extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    // Interface to handle edit and delete actions on observations
    public interface Listener {
        void onEdit(Observation observation);
        void onDelete(Observation observation);
    }

    private List<Observation> observations;
    private Listener listener;

    public ObservationAdapter(List<Observation> observations, Listener listener) {
        this.observations = observations;
        this.listener = listener;
    }

    // Update list content dynamically
    public void updateList(List<Observation> observations) {
        this.observations = observations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_observation layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observations.get(position);

        holder.tvObservation.setText(observation.getObservation());
        holder.tvTime.setText("Time: " + observation.getTime());
        holder.tvComments.setText("Comments: " + observation.getComments());

        // Button listeners
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(observation));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(observation));
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    // ViewHolder class to map UI views for an observation item
    static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvObservation, tvTime, tvComments;
        Button btnEdit, btnDelete;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvObservation = itemView.findViewById(R.id.tvObservation);
            tvTime = itemView.findViewById(R.id.tvObservationTime);
            tvComments = itemView.findViewById(R.id.tvObservationComments);
            btnEdit = itemView.findViewById(R.id.btnEditObservation);
            btnDelete = itemView.findViewById(R.id.btnDeleteObservation);
        }
    }
}