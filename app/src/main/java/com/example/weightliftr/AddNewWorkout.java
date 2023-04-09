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
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        Button newWorkoutBut = (Button) findViewById(R.id.backBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(AddNewWorkout.this, MainActivity.class))
        );

        Button newExBut = (Button) findViewById(R.id.newExBut);
        newExBut.setOnClickListener(event -> {
            // TODO: Show nodes to input new exercise details
            // TODO: Second press to confirm exercise and add to current list of exercises
        });

        Button createBut = (Button) findViewById(R.id.createBut);
        createBut.setOnClickListener(event -> {
            // TODO: Save inputted workout details to DB
        });
    }
}
