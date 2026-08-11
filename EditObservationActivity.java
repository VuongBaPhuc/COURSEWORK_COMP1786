package com.example.mhike;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Observation;

import java.util.Calendar;

// Activity to edit an existing observation
public class EditObservationActivity
        extends AppCompatActivity {

    private EditText etObservation, etTime, etComments;
    private DatabaseHelper databaseHelper;
    private int observationId, hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        databaseHelper = new DatabaseHelper(this);
        // ID of the observation to be edited
        observationId = getIntent().getIntExtra("OBSERVATION_ID", -1);

        etObservation = findViewById(R.id.etEditObservation);
        etTime = findViewById(R.id.etEditObservationTime);
        etComments = findViewById(R.id.etEditObservationComments);
        Button btnUpdate = findViewById(R.id.btnUpdateObservation);

        etTime.setOnClickListener(v -> showDateTimePicker());

        // Load data into fields
        loadObservation();

        btnUpdate.setOnClickListener(v -> updateObservation());
    }

    // Fetch observation data from database
    private void loadObservation() {
        Observation observation = databaseHelper.getObservationById(observationId);
        if (observation == null) {
            Toast.makeText(this, "Observation not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        hikeId = observation.getHikeId();
        etObservation.setText(observation.getObservation());
        etTime.setText(observation.getTime());
        etComments.setText(observation.getComments());
    }

    // DateTime picker for editing the timestamp
    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    TimePickerDialog timeDialog = new TimePickerDialog(
                            this,
                            (timeView, hour, minute) -> {
                                String result = String.format("%02d/%02d/%04d %02d:%02d", day, month + 1, year, hour, minute);
                                etTime.setText(result);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timeDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dateDialog.show();
    }

    // Validate and save updated observation
    private void updateObservation() {
        String observation = etObservation.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String comments = etComments.getText().toString().trim();

        if (observation.isEmpty()) {
            etObservation.setError("Observation is required");
            return;
        }

        Observation item = new Observation(observationId, hikeId, observation, time, comments);
        int result = databaseHelper.updateObservation(item);

        if (result > 0) {
            Toast.makeText(this, "Observation updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}