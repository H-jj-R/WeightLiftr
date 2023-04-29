package com.example.weightliftr.objects;

import java.util.List;

/**
 * Object for storing details of a workout.
 */
public class Workout {
    private long id;
    private String name;
    private List<Exercise> exercises;

    public Workout(String name, List<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}
