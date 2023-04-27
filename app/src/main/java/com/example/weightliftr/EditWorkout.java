package com.example.weightliftr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditWorkout extends AppCompatActivity {

    private Button backBut;
    private WorkoutDBHandlerKotlin WorkoutDBHandler;

    private Workout currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        WorkoutDBHandler = new WorkoutDBHandlerKotlin(this.getApplicationContext());

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(EditWorkout.this, MainActivity.class))
        );

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        List<Workout> workouts = WorkoutDBHandler.getAllWorkouts();

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_workout_list_item, linearLayout, false);

            TextView workoutName = view.findViewById(R.id.workoutName);
            ImageButton startBut = view.findViewById(R.id.startBut);

            workoutName.setText(workouts.get(i).getName());
            startBut.setImageResource(R.drawable.ic_baseline_edit);
            startBut.setId(i);

            startBut.setOnClickListener(v -> {
                linearLayout.removeAllViews();
                View newView = getLayoutInflater().inflate(R.layout.edit_workout_details, linearLayout, false);
                linearLayout.addView(newView);

                currentWorkout = workouts.get(v.getId());
                TextView tv = newView.findViewById(R.id.titleTextView);
                tv.setText(currentWorkout.getName());
                EditText workoutNameEditText = newView.findViewById(R.id.workoutNameEditText);

                LinearLayout verticalLayout = newView.findViewById(R.id.verticalLayout);
                for (int j = 0; j < currentWorkout.getExercises().size(); j++) {

                    View newNewView = getLayoutInflater().inflate(R.layout.edit_workout_exercises, verticalLayout, false);
                    verticalLayout.addView(newNewView);

                    // TODO: This will not work, need to change scope of some variables here

                    EditText nameEditText = newNewView.findViewById(R.id.nameEditText);
                    nameEditText.setId(j);
                    EditText setsEditText = newNewView.findViewById(R.id.setsEditText);
                    setsEditText.setId(j);
                    EditText repsEditText = newNewView.findViewById(R.id.repsEditText);
                    repsEditText.setId(j);
                    EditText restTimeEditText = newNewView.findViewById(R.id.restTimeEditText);
                    restTimeEditText.setId(j);

                    List<Exercise> updatedExercises = new ArrayList<>();

                    Button saveBut = newView.findViewById(R.id.saveBut);
                    saveBut.setOnClickListener(v1 -> {
                        try {
                            if (!workoutNameEditText.getText().toString().equals("")
                                    && !nameEditText.getText().toString().equals("")
                                    && !setsEditText.getText().toString().equals("")
                                    && !repsEditText.getText().toString().equals("")
                                    && !restTimeEditText.getText().toString().equals("")) {
                                long id = workouts.get(v.getId()).getId();
                                String wName = workoutNameEditText.getText().toString();
                                String exName = nameEditText.getText().toString();
                                int sets = Integer.parseInt(setsEditText.getText().toString());
                                int reps = Integer.parseInt(repsEditText.getText().toString());
                                int restTime = Integer.parseInt(restTimeEditText.getText().toString());
                                for (int k = 0; k < currentWorkout.getExercises().size(); k++) {
                                    updatedExercises.add(new Exercise(exName, sets, reps, restTime));
                                }
                                Workout updatedWorkout = new Workout(wName, updatedExercises);
                                updatedWorkout.setId(id);
                                WorkoutDBHandler.updateWorkout(updatedWorkout);
                            } else {
                                sendWarning("Input not valid!");
                            }
                        } catch (Exception e) {
                            sendWarning("Input not valid!");
                        }
                     });

                }

            });
            linearLayout.addView(view);
        }
    }

    public void sendWarning(String message) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
        Toast.makeText(EditWorkout.this, message, Toast.LENGTH_SHORT).show();
    }
}