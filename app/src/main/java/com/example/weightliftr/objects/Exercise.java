package com.example.weightliftr.objects;

/**
 * Object for storing details of an exercise.
 */
public class Exercise {
    private String name;
    private int sets;
    private int reps;
    private int restTime;

    public Exercise(String name, int sets, int reps, int restTime) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.restTime = restTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public int getRestTime() {
        return restTime;
    }
}
