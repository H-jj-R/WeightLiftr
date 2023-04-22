package com.example.weightliftr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button newWorkoutBut;
    private Button editWorkoutBut;
    private Button pastWorkoutsBut;
    private Button startWorkoutBut;
    private Button suggestionBut1;
    private Button suggestionBut2;
    private DBHandler DBHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler = new DBHandler(this.getApplicationContext());


        newWorkoutBut = findViewById(R.id.newWorkoutBut);
        editWorkoutBut = findViewById(R.id.editWorkoutBut);
        pastWorkoutsBut = findViewById(R.id.allWorkoutsBut);
        startWorkoutBut = findViewById(R.id.startWorkoutBut);
        suggestionBut1 = findViewById(R.id.suggestionBut1);
        suggestionBut2 = findViewById(R.id.suggestionBut2);

        setButtonClickEvent(newWorkoutBut, AddNewWorkout.class);
        setButtonClickEvent(editWorkoutBut, EditWorkout.class);
        setButtonClickEvent(pastWorkoutsBut, ViewAllWorkouts.class);
        setButtonClickEvent(startWorkoutBut, StartWorkout.class);

        String[] activityNames = { (String) newWorkoutBut.getText(),
                                         (String) editWorkoutBut.getText(),
                                         (String) pastWorkoutsBut.getText() };
        Class<?>[] activityClasses = { AddNewWorkout.class,
                                             EditWorkout.class,
                                             ViewAllWorkouts.class };

        int randomIndex1 = new Random().nextInt(activityClasses.length);
        suggestionBut1.setText(activityNames[randomIndex1] + "?");
        setButtonClickEvent(suggestionBut1, activityClasses[randomIndex1]);

        int randomIndex2;
        do {
            randomIndex2 = new Random().nextInt(activityClasses.length);
        } while (randomIndex2 == randomIndex1);
        suggestionBut2.setText(activityNames[randomIndex2] + "?");
        setButtonClickEvent(suggestionBut2, activityClasses[randomIndex2]);

//        List<Exercise> exercises= new ArrayList<>();
//        exercises.add(new Exercise("Quad Pull", 3, 10, 60));
//        exercises.add(new Exercise("Squat", 3, 10, 60));
//        exercises.add(new Exercise("Calf Raise", 3, 15, 60));
//
//        Workout w = new Workout("Leg Workout", exercises);
//        DBHandler.insertWorkout(w);
//
//        List<Exercise> exercises1 = new ArrayList<>();
//        exercises1.add(new Exercise("Bicep Curls", 4, 10, 35));
//        exercises1.add(new Exercise("Tricep Pulls", 4, 10, 35));
//        exercises1.add(new Exercise("Forearm Stretches", 3, 15, 35));
//
//        Workout w1 = new Workout("Ind. Muscle Workout", exercises1);
//        DBHandler.insertWorkout(w1);
//
//        List<Exercise> exercises2 = new ArrayList<>();
//        exercises2.add(new Exercise("Bench Press", 3, 10, 110));
//        exercises2.add(new Exercise("Seated Row", 3, 10, 110));
//        exercises2.add(new Exercise("Lat Pull", 2, 15, 110));
//
//        Workout w2 = new Workout("Upper Workout", exercises2);
//        DBHandler.insertWorkout(w2);

    }
    private void setButtonClickEvent(Button button, Class<?> nextActivityClass) {
        button.setOnClickListener(event -> {
            startActivity(new Intent(MainActivity.this, nextActivityClass));
        });
    }
}