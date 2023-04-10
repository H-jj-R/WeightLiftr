package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        Button newWorkoutBut = (Button) findViewById(R.id.newWorkoutBut);
        Button editWorkoutBut = (Button) findViewById(R.id.editWorkoutBut);
        Button pastWorkoutsBut = (Button) findViewById(R.id.pastWorkoutsBut);
        Button startWorkoutBut = (Button) findViewById(R.id.startWorkoutBut);

        setButtonClickEvent(newWorkoutBut, AddNewWorkout.class);
        setButtonClickEvent(editWorkoutBut, EditWorkout.class);
        setButtonClickEvent(pastWorkoutsBut, ViewPastWorkouts.class);
        setButtonClickEvent(startWorkoutBut, StartWorkout.class);

        Button suggestionBut = (Button) findViewById(R.id.suggestionBut);
        String[] activityNames = { (String) newWorkoutBut.getText(),
                                         (String) editWorkoutBut.getText(),
                                         (String) pastWorkoutsBut.getText() };
        Class<?>[] activityClasses = { AddNewWorkout.class,
                                             EditWorkout.class,
                                             ViewPastWorkouts.class };
        int randomIndex = new Random().nextInt(activityClasses.length);
        suggestionBut.setText(activityNames[randomIndex] + "?");
        Class<?> nextActivityClass = activityClasses[randomIndex];
        suggestionBut.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, nextActivityClass))
        );

        //TODO: Maybe implement AdView if SO question is answered, if not add something else before submission

//        List<Exercise> exercises= new ArrayList<>();
//        exercises.add(new Exercise("Bicep Curls", 3, 15));
//        exercises.add(new Exercise("Tricep Pulls", 3, 15));
//        exercises.add(new Exercise("Forearm Stretches", 3, 15));
//
//        Workout w = new Workout("Individual Muscle Workout", exercises);
//        DBHandler.insertWorkout(w);

    }
    private void setButtonClickEvent(Button button, Class<?> nextActivityClass) {
        button.setOnClickListener(event -> startActivity(new Intent(MainActivity.this, nextActivityClass)));
    }
}