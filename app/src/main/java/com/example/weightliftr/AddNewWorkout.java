package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AddNewWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);

        Button newWorkoutBut = (Button) findViewById(R.id.backBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(AddNewWorkout.this, MainActivity.class))
        );
    }
}