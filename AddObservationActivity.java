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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// Activity to add a new observation to a specific hike
public class AddObservationActivity
        extends AppCompatActivity {

    private EditText etObservation, etTime, etComments;
    private DatabaseHelper databaseHelper;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        databaseHelper = new DatabaseHelper(this);
        // Hike ID passed from the Hike list or details screen
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        etObservation = findViewById(R.id.etObservation);
        etTime = findViewById(R.id.etObservationTime);
        etComments = findViewById(R.id.etObservationComments);
        Button btnSave = findViewById(R.id.btnSaveObservation);

        // Pre-fill current time for convenience
        setCurrentDateTime();

        // DateTime selection listeners
        etTime.setOnClickListener(v -> showDateTimePicker());
        btnSave.setOnClickListener(v -> saveObservation());
    }

    // Set the current system time in dd/MM/yyyy HH:mm format
    private void setCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        etTime.setText(format.format(Calendar.getInstance().getTime()));
    }

    // Display DatePicker followed by TimePicker
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

    // Validate inputs and save the observation to the database
    private void saveObservation() {
        String observation = etObservation.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String comments = etComments.getText().toString().trim();

        if (observation.isEmpty()) {
            etObservation.setError("Observation is required");
            etObservation.requestFocus();
            return;
        }

        Observation item = new Observation(hikeId, observation, time, comments);
        long result = databaseHelper.addObservation(item);

        if (result != -1) {
            Toast.makeText(this, "Observation saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save observation", Toast.LENGTH_SHORT).show();
        }
    }
}