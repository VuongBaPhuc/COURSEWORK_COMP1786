package com.example.mhike.model;

// Data model class representing an Observation for a Hike
public class Observation {

    private int id;
    private int hikeId;
    private String observation;
    private String time;
    private String comments;

    // Constructor for creating a new Observation (without ID)
    public Observation(
            int hikeId,
            String observation,
            String time,
            String comments
    ) {
        this.hikeId = hikeId;
        this.observation = observation;
        this.time = time;
        this.comments = comments;
    }

    // Constructor for creating an Observation from database (with ID)
    public Observation(
            int id,
            int hikeId,
            String observation,
            String time,
            String comments
    ) {
        this.id = id;
        this.hikeId = hikeId;
        this.observation = observation;
        this.time = time;
        this.comments = comments;
    }

    // Getters and Setters for Observation properties
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getHikeId() { return hikeId; }
    public void setHikeId(int hikeId) { this.hikeId = hikeId; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}