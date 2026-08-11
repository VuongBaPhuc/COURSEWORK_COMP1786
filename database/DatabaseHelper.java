package com.example.mhike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mhike.model.Hike;
import com.example.mhike.model.Observation;

import java.util.ArrayList;
import java.util.List;

// SQLite Database Helper class for managing MHike application data
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mhike.db";
    private static final int DATABASE_VERSION = 1;

    // Hike table constants
    public static final String TABLE_HIKES = "hikes";
    public static final String Hike_ID = "id";
    public static final String Hike_NAME = "name";
    public static final String Hike_LOCATION = "location";
    public static final String Hike_DATE = "date";
    public static final String Hike_PARKING = "parking";
    public static final String Hike_LENGTH = "length";
    public static final String Hike_DIFFICULTY = "difficulty";
    public static final String Hike_DESCRIPTION = "description";
    public static final String Hike_WEATHER = "weather";
    public static final String Hike_DURATION = "duration";

    // Observation table constants
    public static final String TABLE_OBSERVATIONS = "observations";
    public static final String OBS_ID = "id";
    public static final String OBS_HIKE_ID = "hike_id";
    public static final String OBS_OBSERVATION = "observation";
    public static final String OBS_TIME = "time";
    public static final String OBS_COMMENTS = "comments";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Enable foreign key constraints support
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Hikes table
        String createHikesTable = "CREATE TABLE " + TABLE_HIKES + " (" +
                        Hike_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Hike_NAME + " TEXT NOT NULL, " +
                        Hike_LOCATION + " TEXT NOT NULL, " +
                        Hike_DATE + " TEXT NOT NULL, " +
                        Hike_PARKING + " TEXT NOT NULL, " +
                        Hike_LENGTH + " TEXT NOT NULL, " +
                        Hike_DIFFICULTY + " TEXT NOT NULL, " +
                        Hike_DESCRIPTION + " TEXT, " +
                        Hike_WEATHER + " TEXT, " +
                        Hike_DURATION + " TEXT" +
                        ")";
        db.execSQL(createHikesTable);

        // Create Observations table with a foreign key referencing Hikes
        String createObservationsTable = "CREATE TABLE " + TABLE_OBSERVATIONS + " (" +
                        OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        OBS_HIKE_ID + " INTEGER NOT NULL, " +
                        OBS_OBSERVATION + " TEXT NOT NULL, " +
                        OBS_TIME + " TEXT NOT NULL, " +
                        OBS_COMMENTS + " TEXT, " +
                        "FOREIGN KEY(" + OBS_HIKE_ID + ") REFERENCES " +
                        TABLE_HIKES + "(" + Hike_ID + ") ON DELETE CASCADE" +
                        ")";
        db.execSQL(createObservationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate them on version upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    // --- HIKE: Data access methods (CRUD) ---

    // Insert a new hike record
    public long addHike(Hike hike) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hike_NAME, hike.getName());
        values.put(Hike_LOCATION, hike.getLocation());
        values.put(Hike_DATE, hike.getDate());
        values.put(Hike_PARKING, hike.getParking());
        values.put(Hike_LENGTH, hike.getLength());
        values.put(Hike_DIFFICULTY, hike.getDifficulty());
        values.put(Hike_DESCRIPTION, hike.getDescription());
        values.put(Hike_WEATHER, hike.getWeather());
        values.put(Hike_DURATION, hike.getDuration());
        return db.insert(TABLE_HIKES, null, values);
    }

    // Retrieve all hikes sorted by newest first
    public List<Hike> getAllHikes() {
        List<Hike> hikes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIKES, null, null, null, null, null, Hike_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                hikes.add(cursorToHike(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hikes;
    }

    // Fetch a single hike by its ID
    public Hike getHikeById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIKES, null, Hike_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Hike hike = null;
        if (cursor.moveToFirst()) {
            hike = cursorToHike(cursor);
        }
        cursor.close();
        return hike;
    }

    // Update an existing hike's information
    public int updateHike(Hike hike) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hike_NAME, hike.getName());
        values.put(Hike_LOCATION, hike.getLocation());
        values.put(Hike_DATE, hike.getDate());
        values.put(Hike_PARKING, hike.getParking());
        values.put(Hike_LENGTH, hike.getLength());
        values.put(Hike_DIFFICULTY, hike.getDifficulty());
        values.put(Hike_DESCRIPTION, hike.getDescription());
        values.put(Hike_WEATHER, hike.getWeather());
        values.put(Hike_DURATION, hike.getDuration());
        return db.update(TABLE_HIKES, values, Hike_ID + "=?", new String[]{String.valueOf(hike.getId())});
    }

    // Delete a specific hike by ID
    public int deleteHike(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_HIKES, Hike_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Clear all records from the hikes table
    public void deleteAllHikes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HIKES, null, null);
    }

    // Search hikes based on provided criteria (name, location, length, date)
    public List<Hike> searchHikes(String name, String location, String length, String date) {
        List<Hike> hikes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder selection = new StringBuilder();
        List<String> args = new ArrayList<>();

        if (!name.isEmpty()) {
            selection.append(Hike_NAME).append(" LIKE ? ");
            args.add("%" + name + "%");
        }
        if (!location.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(Hike_LOCATION).append(" LIKE ? ");
            args.add("%" + location + "%");
        }
        if (!length.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(Hike_LENGTH).append(" LIKE ? ");
            args.add("%" + length + "%");
        }
        if (!date.isEmpty()) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(Hike_DATE).append(" LIKE ? ");
            args.add("%" + date + "%");
        }

        Cursor cursor = db.query(TABLE_HIKES, null, selection.length() == 0 ? null : selection.toString(), args.toArray(new String[0]), null, null, Hike_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                hikes.add(cursorToHike(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hikes;
    }

    // --- OBSERVATION: Observation related methods ---

    // Add a new observation for a specific hike
    public long addObservation(Observation observation) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBS_HIKE_ID, observation.getHikeId());
        values.put(OBS_OBSERVATION, observation.getObservation());
        values.put(OBS_TIME, observation.getTime());
        values.put(OBS_COMMENTS, observation.getComments());
        return db.insert(TABLE_OBSERVATIONS, null, values);
    }

    // Get all observations linked to a specific hike ID
    public List<Observation> getObservationsForHike(int hikeId) {
        List<Observation> observations = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBSERVATIONS, null, OBS_HIKE_ID + "=?", new String[]{String.valueOf(hikeId)}, null, null, OBS_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                observations.add(cursorToObservation(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return observations;
    }

    // Update details for an observation
    public int updateObservation(Observation observation) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBS_OBSERVATION, observation.getObservation());
        values.put(OBS_TIME, observation.getTime());
        values.put(OBS_COMMENTS, observation.getComments());
        return db.update(TABLE_OBSERVATIONS, values, OBS_ID + "=?", new String[]{String.valueOf(observation.getId())});
    }

    // Delete a specific observation by ID
    public int deleteObservation(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_OBSERVATIONS, OBS_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Helper method to map a Cursor row to a Hike object
    private Hike cursorToHike(Cursor cursor) {
        return new Hike(
                cursor.getInt(cursor.getColumnIndexOrThrow(Hike_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_PARKING)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_LENGTH)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_DIFFICULTY)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_WEATHER)),
                cursor.getString(cursor.getColumnIndexOrThrow(Hike_DURATION))
        );
    }

    // Helper method to map a Cursor row to an Observation object
    private Observation cursorToObservation(Cursor cursor) {
        return new Observation(
                cursor.getInt(cursor.getColumnIndexOrThrow(OBS_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(OBS_HIKE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(OBS_OBSERVATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(OBS_TIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(OBS_COMMENTS))
        );
    }
}