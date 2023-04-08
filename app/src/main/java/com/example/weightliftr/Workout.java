package com.example.weightliftr;

import java.util.List;

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
