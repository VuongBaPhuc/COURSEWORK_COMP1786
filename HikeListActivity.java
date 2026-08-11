package com.example.mhike;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.adapter.HikeAdapter;
import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;

import java.util.Calendar;
import java.util.List;

// Activity to display a list of all hikes with search and delete functionality
public class HikeListActivity
        extends AppCompatActivity
        implements HikeAdapter.OnHikeActionListener {

    // UI component declarations
    private RecyclerView recyclerHikes;
    private EditText etSearchName;
    private EditText etSearchLocation;
    private EditText etSearchLength;
    private EditText etSearchDate;
    private Button btnSearch;
    private Button btnClearSearch;
    private Button btnDeleteAll;

    private DatabaseHelper databaseHelper;
    private HikeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        // Database and RecyclerView setup
        databaseHelper = new DatabaseHelper(this);
        recyclerHikes = findViewById(R.id.recyclerHikes);
        recyclerHikes.setLayoutManager(new LinearLayoutManager(this));

        // UI mapping
        etSearchName = findViewById(R.id.etSearchName);
        etSearchLocation = findViewById(R.id.etSearchLocation);
        etSearchLength = findViewById(R.id.etSearchLength);
        etSearchDate = findViewById(R.id.etSearchDate);
        btnSearch = findViewById(R.id.btnSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);

        // Click listeners for search and delete actions
        etSearchDate.setOnClickListener(v -> showDatePicker());
        btnSearch.setOnClickListener(v -> searchHikes());
        btnClearSearch.setOnClickListener(v -> clearSearch());
        btnDeleteAll.setOnClickListener(v -> confirmDeleteAll());

        // Initial data load
        loadHikes();
    }

    // Refresh list whenever activity returns to foreground
    @Override
    protected void onResume() {
        super.onResume();
        loadHikes();
    }

    // Fetch all hikes from DB and update the adapter
    private void loadHikes() {
        List<Hike> hikes = databaseHelper.getAllHikes();
        adapter = new HikeAdapter(hikes, this);
        recyclerHikes.setAdapter(adapter);
    }

    // Filter hikes based on search input fields
    private void searchHikes() {
        String name = etSearchName.getText().toString().trim();
        String location = etSearchLocation.getText().toString().trim();
        String length = etSearchLength.getText().toString().trim();
        String date = etSearchDate.getText().toString().trim();

        List<Hike> results = databaseHelper.searchHikes(name, location, length, date);
        adapter.updateList(results);
        Toast.makeText(this, results.size() + " hike(s) found", Toast.LENGTH_SHORT).show();
    }

    // Reset search fields and reload all hikes
    private void clearSearch() {
        etSearchName.setText("");
        etSearchLocation.setText("");
        etSearchLength.setText("");
        etSearchDate.setText("");
        loadHikes();
    }

    // Display date picker for the search criteria
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String date = String.format("%02d/%02d/%04d", day, month + 1, year);
                    etSearchDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // Confirmation dialog before deleting all records
    private void confirmDeleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Hikes")
                .setMessage("Are you sure you want to delete all hikes?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete All", (dialog, which) -> {
                    databaseHelper.deleteAllHikes();
                    loadHikes();
                    Toast.makeText(this, "All hikes deleted", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // Handle Edit action from the adapter
    @Override
    public void onEdit(Hike hike) {
        Intent intent = new Intent(this, EditHikeActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    // Handle Delete action for a single hike
    @Override
    public void onDelete(Hike hike) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Delete " + hike.getName() + "?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    databaseHelper.deleteHike(hike.getId());
                    loadHikes();
                    Toast.makeText(this, "Hike deleted", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // Handle navigation to Observation list for a hike
    @Override
    public void onObservations(Hike hike) {
        Intent intent = new Intent(this, ObservationListActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        intent.putExtra("HIKE_NAME", hike.getName());
        startActivity(intent);
    }
}