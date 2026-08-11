package com.example.mhike;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;

import java.util.Calendar;

// Activity for editing an existing hike record
public class EditHikeActivity extends AppCompatActivity {

    // UI elements
    private EditText etName, etLocation, etDate, etLength, etDescription, etWeather, etDuration;
    private Spinner spinnerParking, spinnerDifficulty;

    private DatabaseHelper databaseHelper;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);

        databaseHelper = new DatabaseHelper(this);
        // Get hike ID passed from the previous activity
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        // Bind UI components
        etName = findViewById(R.id.etEditHikeName);
        etLocation = findViewById(R.id.etEditLocation);
        etDate = findViewById(R.id.etEditDate);
        etLength = findViewById(R.id.etEditLength);
        etDescription = findViewById(R.id.etEditDescription);
        etWeather = findViewById(R.id.etEditWeather);
        etDuration = findViewById(R.id.etEditDuration);
        spinnerParking = findViewById(R.id.spinnerEditParking);
        spinnerDifficulty = findViewById(R.id.spinnerEditDifficulty);
        Button btnUpdate = findViewById(R.id.btnUpdateHike);

        setupSpinners();
        etDate.setOnClickListener(v -> showDatePicker());

        // Load existing hike data into the form
        loadHike();

        // Update button click listener
        btnUpdate.setOnClickListener(v -> updateHike());
    }

    // Initialize spinners with pre-defined options
    private void setupSpinners() {
        String[] parking = {"Yes", "No"};
        ArrayAdapter<String> parkingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, parking);
        parkingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParking.setAdapter(parkingAdapter);

        String[] difficulty = {"Easy", "Moderate", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulty);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
    }

    // Fetch hike from database and populate the fields
    private void loadHike() {
        Hike hike = databaseHelper.getHikeById(hikeId);
        if (hike == null) {
            Toast.makeText(this, "Hike not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etName.setText(hike.getName());
        etLocation.setText(hike.getLocation());
        etDate.setText(hike.getDate());
        etLength.setText(hike.getLength());
        etDescription.setText(hike.getDescription());
        etWeather.setText(hike.getWeather());
        etDuration.setText(hike.getDuration());

        setSpinnerValue(spinnerParking, hike.getParking());
        setSpinnerValue(spinnerDifficulty, hike.getDifficulty());
    }

    // Helper to set a spinner to a specific string value
    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) spinner.setSelection(position);
    }

    // Display date picker dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String date = String.format("%02d/%02d/%04d", day, month + 1, year);
                    etDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // Validate inputs and update the hike in the database
    private void updateHike() {
        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String parking = spinnerParking.getSelectedItem().toString();
        String length = etLength.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        String weather = etWeather.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();

        // Validation
        if (name.isEmpty()) { etName.setError("Required"); return; }
        if (location.isEmpty()) { etLocation.setError("Required"); return; }
        if (date.isEmpty()) { etDate.setError("Required"); return; }
        if (length.isEmpty()) { etLength.setError("Required"); return; }

        Hike hike = new Hike(hikeId, name, location, date, parking, length, difficulty, description, weather, duration);
        int result = databaseHelper.updateHike(hike);

        if (result > 0) {
            Toast.makeText(this, "Hike updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}