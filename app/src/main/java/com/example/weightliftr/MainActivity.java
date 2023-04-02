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

        Button newWorkoutBut = (Button) findViewById(R.id.newWorkoutBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, AddNewWorkout.class))
        );

        Button editWorkoutBut = (Button) findViewById(R.id.editWorkoutBut);
        editWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, EditWorkout.class))
        );

        Button pastWorkoutsBut = (Button) findViewById(R.id.pastWorkoutsBut);
        pastWorkoutsBut.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, ViewPastWorkouts.class))
        );

        Button startWorkoutBut = (Button) findViewById(R.id.startWorkoutBut);
        startWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, StartWorkout.class))
        );

        Button suggestionBut = (Button) findViewById(R.id.suggestionBut);
        int rnd = new Random().nextInt(3);
        Class c;
        switch (rnd) {
            case 0:
                suggestionBut.setText(newWorkoutBut.getText() + "?");
                c = AddNewWorkout.class;
                break;
            case 1:
                suggestionBut.setText(editWorkoutBut.getText() + "?");
                c = EditWorkout.class;
                break;
            case 2:
                suggestionBut.setText(pastWorkoutsBut.getText() + "?");
                c = ViewPastWorkouts.class;
                break;
            default:
                c = null;
        }
        suggestionBut.setOnClickListener(event ->
                startActivity(new Intent(MainActivity.this, c))
        );

        //TODO: Maybe implement AdView, if not add 2nd suggestion Button

    }
}