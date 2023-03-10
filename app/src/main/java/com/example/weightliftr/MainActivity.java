package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
        editWorkoutBut.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, EditWorkout.class))
        );

        Button pastWorkoutsBut = (Button) findViewById(R.id.pastWorkoutsBut);
        pastWorkoutsBut.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewPastWorkouts.class))
        );

        Button startWorkoutBut = (Button) findViewById(R.id.startWorkoutBut);
        startWorkoutBut.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, StartWorkout.class))
        );
    }
}