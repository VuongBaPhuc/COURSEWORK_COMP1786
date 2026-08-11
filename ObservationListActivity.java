package com.example.mhike;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.adapter.ObservationAdapter;
import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Observation;

import java.util.List;

// Activity to display all observations for a specific hike
public class ObservationListActivity
        extends AppCompatActivity
        implements ObservationAdapter.Listener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerObservations;
    private ObservationAdapter adapter;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        databaseHelper = new DatabaseHelper(this);
        // Get hike details passed from the Hike List
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);
        String hikeName = getIntent().getStringExtra("HIKE_NAME");

        TextView title = findViewById(R.id.tvObservationTitle);
        title.setText("Observations - " + hikeName);

        recyclerObservations = findViewById(R.id.recyclerObservations);
        Button btnAdd = findViewById(R.id.btnAddObservation);

        recyclerObservations.setLayoutManager(new LinearLayoutManager(this));

        // Navigate to add new observation screen
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddObservationActivity.class);
            intent.putExtra("HIKE_ID", hikeId);
            startActivity(intent);
        });

        loadObservations();
    }

    // Refresh list on return to activity
    @Override
    protected void onResume() {
        super.onResume();
        loadObservations();
    }

    // Load observations related to this hike from database
    private void loadObservations() {
        List<Observation> observations = databaseHelper.getObservationsForHike(hikeId);
        if (adapter == null) {
            adapter = new ObservationAdapter(observations, this);
            recyclerObservations.setAdapter(adapter);
        } else {
            adapter.updateList(observations);
        }
    }

    // Handle Edit action for an observation
    @Override
    public void onEdit(Observation observation) {
        Intent intent = new Intent(this, EditObservationActivity.class);
        intent.putExtra("OBSERVATION_ID", observation.getId());
        startActivity(intent);
    }

    // Handle Delete action for an observation
    @Override
    public void onDelete(Observation observation) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Delete this observation?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    databaseHelper.deleteObservation(observation.getId());
                    loadObservations();
                    Toast.makeText(this, "Observation deleted", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}