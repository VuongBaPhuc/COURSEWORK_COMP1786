package com.example.mhike;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // UI elements declaration
    private EditText etHikeName;
    private EditText etLocation;
    private EditText etDate;
    private EditText etLength;
    private EditText etDescription;
    private EditText etWeather;
    private EditText etDuration;

    private Spinner spinnerParking;
    private Spinner spinnerDifficulty;

    private Button btnSaveHike;
    private Button btnViewHikes;

    // Database helper for storage operations
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity layout
        setContentView(R.layout.activity_main);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Map UI components from XML layout
        etHikeName = findViewById(R.id.etHikeName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etLength = findViewById(R.id.etLength);
        etDescription = findViewById(R.id.etDescription);
        etWeather = findViewById(R.id.etWeather);
        etDuration = findViewById(R.id.etDuration);

        spinnerParking = findViewById(R.id.spinnerParking);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        btnSaveHike = findViewById(R.id.btnSaveHike);
        btnViewHikes = findViewById(R.id.btnViewHikes);

        // Populate Spinners with data
        setupParkingSpinner();
        setupDifficultySpinner();

        // Show date picker dialog on date field click
        etDate.setOnClickListener(v -> showDatePicker());

        // Save button click listener: validate inputs and show confirmation
        btnSaveHike.setOnClickListener(v -> validateAndConfirm());

        // Navigate to the list of hikes
        btnViewHikes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HikeListActivity.class);
            startActivity(intent);
        });
    }

    // Initialize parking options spinner
    private void setupParkingSpinner() {
        String[] options = {
                "Select parking option",
                "Yes",
                "No"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParking.setAdapter(adapter);
    }

    // Initialize difficulty levels spinner
    private void setupDifficultySpinner() {
        String[] options = {
                "Select difficulty",
                "Easy",
                "Moderate",
                "Hard"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    // Display a DatePickerDialog to select hike date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    etDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // Validate input fields and show confirmation dialog if data is valid
    private void validateAndConfirm() {
        String name = etHikeName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String parking = spinnerParking.getSelectedItem().toString();
        String length = etLength.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        String weather = etWeather.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();

        // Validation for required fields
        if (name.isEmpty()) {
            etHikeName.setError("Please enter hike name");
            etHikeName.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            etLocation.setError("Please enter location");
            etLocation.requestFocus();
            return;
        }
        if (date.isEmpty()) {
            etDate.setError("Please select date");
            etDate.requestFocus();
            return;
        }
        if (parking.equals("Select parking option")) {
            Toast.makeText(this, "Please select parking availability", Toast.LENGTH_SHORT).show();
            return;
        }
        if (length.isEmpty()) {
            etLength.setError("Please enter hike length");
            etLength.requestFocus();
            return;
        }
        if (difficulty.equals("Select difficulty")) {
            Toast.makeText(this, "Please select difficulty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Hike object and show confirmation
        Hike hike = new Hike(name, location, date, parking, length, difficulty, description, weather, duration);
        showConfirmation(hike);
    }

    // Show alert dialog to confirm details before saving to database
    private void showConfirmation(Hike hike) {
        String message = "Name: " + hike.getName() +
                        "\n\nLocation: " + hike.getLocation() +
                        "\nDate: " + hike.getDate() +
                        "\nParking: " + hike.getParking() +
                        "\nLength: " + hike.getLength() + " km" +
                        "\nDifficulty: " + hike.getDifficulty() +
                        "\nDescription: " + hike.getDescription() +
                        "\nWeather: " + hike.getWeather() +
                        "\nDuration: " + hike.getDuration();

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike Details")
                .setMessage(message)
                .setNegativeButton("Edit", null)
                .setPositiveButton("Confirm & Save", (dialog, which) -> saveHike(hike))
                .show();
    }

    // Save the hike object to the database and redirect to list screen
    private void saveHike(Hike hike) {
        long result = databaseHelper.addHike(hike);
        if (result != -1) {
            Toast.makeText(this, "Hike saved successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
            Intent intent = new Intent(MainActivity.this, HikeListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to save hike", Toast.LENGTH_SHORT).show();
        }
    }

    // Reset all input fields in the form
    private void clearForm() {
        etHikeName.setText("");
        etLocation.setText("");
        etDate.setText("");
        etLength.setText("");
        etDescription.setText("");
        etWeather.setText("");
        etDuration.setText("");

        spinnerParking.setSelection(0);
        spinnerDifficulty.setSelection(0);
    }
}