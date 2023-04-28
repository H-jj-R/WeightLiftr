package com.example.weightliftr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import java.util.Objects;

public class AddNewWorkout extends AppCompatActivity {

    private WorkoutDBHandler workoutDBHandler;

    private Button newExBut;
    private LinearLayout addExLayout;
    private EditText exNameEditText;
    private EditText exSetsEditText;
    private EditText exRepsEditText;
    private EditText exRestTimeEditText;

    private final List<Exercise> exercisesToAdd = new ArrayList<>();
    private final List<String> exerciseNames = new ArrayList<>();
    private ArrayAdapter<String> exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        Button backBut = findViewById(R.id.backBut);
        Button createBut = findViewById(R.id.createBut);
        newExBut = findViewById(R.id.newExBut);
        addExLayout = findViewById(R.id.addExLayout);
        exNameEditText = findViewById(R.id.exNameEditText);
        exSetsEditText = findViewById(R.id.exSetsEditText);
        exRepsEditText = findViewById(R.id.exRepsEditText);
        exRestTimeEditText = findViewById(R.id.exRestTimeEditText);
        ListView addedExList = findViewById(R.id.addedExList);

        backBut.setOnClickListener(v ->
                startActivity(new Intent(AddNewWorkout.this, MainActivity.class))
        );

        exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
        addedExList.setAdapter(exerciseAdapter);

        newExBut.setOnClickListener(this::newExFunc);

        createBut.setOnClickListener(this::createFunc);
    }

    private void newExFunc(@NonNull View v) {
        if (addExLayout.getVisibility() == View.GONE) {
            addExLayout.setVisibility(View.VISIBLE);
            newExBut.setText(R.string.new_ex_adding);
        } else {
            try {
                if (!exNameEditText.getText().toString().equals("")
                        && !exSetsEditText.getText().toString().equals("")
                        && !exRepsEditText.getText().toString().equals("")
                        && !exRestTimeEditText.getText().toString().equals("")) {
                    String name = exNameEditText.getText().toString();
                    int sets = Integer.parseInt(exSetsEditText.getText().toString());
                    int reps = Integer.parseInt(exRepsEditText.getText().toString());
                    int restTime = Integer.parseInt(exRestTimeEditText.getText().toString());
                    exercisesToAdd.add(new Exercise(name, sets, reps, restTime));
                    exerciseNames.add(name);
                    exerciseAdapter.notifyDataSetChanged();
                    exNameEditText.getText().clear();
                    exSetsEditText.getText().clear();
                    exRepsEditText.getText().clear();
                    exRestTimeEditText.getText().clear();
                    addExLayout.setVisibility(View.GONE);
                    newExBut.setText(R.string.new_ex_not_adding);
                } else {
                    sendToast("Input not valid!");
                }
            } catch (NumberFormatException e) {
                sendToast("Input not valid!");
            }
        }
    }

    private void createFunc(@NonNull View v) {
        EditText workoutNameIn = findViewById(R.id.workoutNameIn);
        if (workoutNameIn.getText().toString().equals("")) {
            sendToast("Workout name field empty!");
        } else if (exercisesToAdd.isEmpty()) {
            sendToast("No exercises have been added!");
        } else {
            Workout w = new Workout(workoutNameIn.getText().toString(), exercisesToAdd);
            workoutDBHandler.insertWorkout(w);
            this.finish();
            startActivity(getIntent());
        }
    }

    private void sendToast(String message) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
        Toast.makeText(AddNewWorkout.this, message, Toast.LENGTH_SHORT).show();
    }
}
