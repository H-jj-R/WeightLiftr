package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;

public class AddNewWorkout extends AppCompatActivity {

    List<Exercise> exercisesToAdd = new ArrayList<>();
    List<String> exerciseNames = new ArrayList<>();
    ArrayAdapter<String> exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        Button newWorkoutBut = findViewById(R.id.backBut);
        Button createBut = findViewById(R.id.createBut);
        Button mShowEditTextButton = findViewById(R.id.newExBut);
        LinearLayout addExLayout = findViewById(R.id.addExLayout);
        EditText exNameEditText = findViewById(R.id.exNameEditText);
        EditText exSetsEditText = findViewById(R.id.exSetsEditText);
        EditText exRepsEditText = findViewById(R.id.exRepsEditText);
        ListView addedExList = findViewById(R.id.addedExList);

        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(AddNewWorkout.this, MainActivity.class))
        );

        exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
        addedExList.setAdapter(exerciseAdapter);

        mShowEditTextButton.setOnClickListener(event -> {
            if (addExLayout.getVisibility() == View.GONE) {
                addExLayout.setVisibility(View.VISIBLE);
                mShowEditTextButton.setText("Add This Exercise");
            } else {
                try {
                    if (!exNameEditText.getText().toString().equals("")
                            && !exSetsEditText.getText().toString().equals("")
                            && !exRepsEditText.getText().toString().equals("")) {
                        String name = exNameEditText.getText().toString();
                        int sets = Integer.parseInt(exSetsEditText.getText().toString());
                        int reps = Integer.parseInt(exRepsEditText.getText().toString());
                        exercisesToAdd.add(new Exercise(name, sets, reps));
                        exerciseNames.add(name + sets + reps);
                        exerciseAdapter.notifyDataSetChanged();
                        exNameEditText.getText().clear();
                        exSetsEditText.getText().clear();
                        exRepsEditText.getText().clear();
                        addExLayout.setVisibility(View.GONE);
                        mShowEditTextButton.setText("Add New Exercise?");
                    } else {
                        Toast.makeText(AddNewWorkout.this, "Input not valid!", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AddNewWorkout.this, "Input not valid!", Toast.LENGTH_LONG).show();
                }
            }
        });

        createBut.setOnClickListener(event -> {
            EditText workoutNameIn = (EditText) findViewById(R.id.workoutNameIn);
            if (workoutNameIn.getText().toString().equals("")) {
                Toast.makeText(AddNewWorkout.this, "Workout name field empty!", Toast.LENGTH_LONG).show();
            } else if (exercisesToAdd.isEmpty()) {
                Toast.makeText(AddNewWorkout.this, "No exercises have been added!", Toast.LENGTH_LONG).show();
            } else {
                Workout w = new Workout(workoutNameIn.getText().toString(), exercisesToAdd);
                DBHandler.insertWorkout(w);
            }
        });
    }
}
