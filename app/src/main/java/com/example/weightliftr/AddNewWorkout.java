package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;

public class AddNewWorkout extends AppCompatActivity {

    String addExState = "Origin";
    List<Exercise> exercisesToAdd = new ArrayList<>();

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
            // TODO: First press to add new exercise
            if (addExState.equals("Origin")) {
                addExState = "Adding";

            // TODO: Second press to confirm exercise and add to current list of exercises
            } else if (addExState.equals("Adding")) {
                addExState = "Origin";
            }
        });

        Button createBut = (Button) findViewById(R.id.createBut);
        createBut.setOnClickListener(event -> {
            EditText workoutNameIn = (EditText) findViewById(R.id.workoutNameIn);
            if (!workoutNameIn.getText().toString().equals("") && !exercisesToAdd.isEmpty()) {
                Workout w = new Workout(workoutNameIn.getText().toString(), exercisesToAdd);
                DBHandler.insertWorkout(w);
            } else if (workoutNameIn.getText().toString().equals("")){
                Toast.makeText(AddNewWorkout.this, "Workout name field empty!", Toast.LENGTH_LONG).show();
            } else if (exercisesToAdd.isEmpty()){
                Toast.makeText(AddNewWorkout.this, "No exercises have been added!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
