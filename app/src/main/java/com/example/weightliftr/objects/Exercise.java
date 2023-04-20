package com.example.weightliftr.objects;

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
