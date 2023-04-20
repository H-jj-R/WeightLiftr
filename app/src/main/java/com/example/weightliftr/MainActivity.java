package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;

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
//        exercises.add(new Exercise("Quad Pull", 3, 10));
//        exercises.add(new Exercise("Squat", 3, 10));
//        exercises.add(new Exercise("Calf Raise", 3, 15));
//
//        Workout w = new Workout("Leg Workout", exercises);
//        DBHandler.insertWorkout(w);

    }
    private void setButtonClickEvent(Button button, Class<?> nextActivityClass) {
        button.setOnClickListener(event -> {
            startActivity(new Intent(MainActivity.this, nextActivityClass));
        });
    }
}