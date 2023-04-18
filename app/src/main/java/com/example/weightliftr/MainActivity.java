package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

        Button suggestionBut1 = (Button) findViewById(R.id.suggestionBut1);
        Button suggestionBut2 = (Button) findViewById(R.id.suggestionBut2);
        String[] activityNames = { (String) newWorkoutBut.getText(),
                                         (String) editWorkoutBut.getText(),
                                         (String) pastWorkoutsBut.getText() };
        Class<?>[] activityClasses = { AddNewWorkout.class,
                                             EditWorkout.class,
                                             ViewPastWorkouts.class };
        int randomIndex1 = new Random().nextInt(activityClasses.length);
        suggestionBut1.setText(activityNames[randomIndex1] + "?");
        Class<?> nextActivityClass1 = activityClasses[randomIndex1];
        suggestionBut1.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, nextActivityClass1))
        );
        int randomIndex2 = new Random().nextInt(activityClasses.length);
        while (randomIndex2 == randomIndex1) {
            randomIndex2 = new Random().nextInt(activityClasses.length);
        }
        suggestionBut2.setText(activityNames[randomIndex2] + "?");
        Class<?> nextActivityClass2 = activityClasses[randomIndex2];
        suggestionBut2.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, nextActivityClass2))
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