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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditWorkout extends AppCompatActivity {

    private WorkoutDBHandler WorkoutDBHandler;

    private Button backBut;
    private LinearLayout linearLayout;
    private TextView workoutName;
    private ImageButton startBut;
    private TextView tv;
    private EditText workoutNameEditText;
    private LinearLayout verticalLayout;
    private EditText nameEditText;
    private EditText setsEditText;
    private EditText repsEditText;
    private EditText restTimeEditText;

    private List<Workout> workouts;
    private Exercise exercise;
    private Map<Exercise, Map<String, EditText>> exerciseEditTexts;
    private Map<String, EditText> editTexts;

    private Workout currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        WorkoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(EditWorkout.this, MainActivity.class))
        );

        linearLayout = findViewById(R.id.linearLayout);

        workouts = WorkoutDBHandler.getAllWorkouts();

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_workout_list_item, linearLayout, false);

            workoutName = view.findViewById(R.id.workoutName);
            startBut = view.findViewById(R.id.startBut);

            workoutName.setText(workouts.get(i).getName());
            startBut.setImageResource(R.drawable.ic_baseline_edit);
            startBut.setId(i);

            startBut.setOnClickListener(this::startButFunc);
            linearLayout.addView(view);
        }
    }

    private void startButFunc(View v) {
        linearLayout.removeAllViews();
        View baseEditView = getLayoutInflater().inflate(R.layout.edit_workout_details, linearLayout, false);
        linearLayout.addView(baseEditView);

        currentWorkout = workouts.get(v.getId());
        tv = baseEditView.findViewById(R.id.titleTextView);
        tv.setText(currentWorkout.getName());
        workoutNameEditText = baseEditView.findViewById(R.id.workoutNameEditText);
        workoutNameEditText.setText(currentWorkout.getName());
        verticalLayout = baseEditView.findViewById(R.id.verticalLayout);

        exerciseEditTexts = new HashMap<>();

        for (int i = 0; i < currentWorkout.getExercises().size(); i++) {
            View exerciseEditView = getLayoutInflater().inflate(R.layout.edit_workout_exercises, verticalLayout, false);
            verticalLayout.addView(exerciseEditView);

            nameEditText = exerciseEditView.findViewById(R.id.nameEditText);
            setsEditText = exerciseEditView.findViewById(R.id.setsEditText);
            repsEditText = exerciseEditView.findViewById(R.id.repsEditText);
            restTimeEditText = exerciseEditView.findViewById(R.id.restTimeEditText);

            nameEditText.setText(currentWorkout.getExercises().get(i).getName());
            setsEditText.setText(String.valueOf(currentWorkout.getExercises().get(i).getSets()));
            repsEditText.setText(String.valueOf(currentWorkout.getExercises().get(i).getReps()));
            restTimeEditText.setText(String.valueOf(currentWorkout.getExercises().get(i).getRestTime()));

            exercise = new Exercise(nameEditText.getText().toString(),
                    Integer.parseInt(setsEditText.getText().toString()),
                    Integer.parseInt(repsEditText.getText().toString()),
                    Integer.parseInt(restTimeEditText.getText().toString()));

            editTexts = new HashMap<>();
            editTexts.put("name", nameEditText);
            editTexts.put("sets", setsEditText);
            editTexts.put("reps", repsEditText);
            editTexts.put("restTime", restTimeEditText);
            exerciseEditTexts.put(exercise, editTexts);
        }

        Button saveBut = baseEditView.findViewById(R.id.saveBut);
        saveBut.setOnClickListener(this::saveButFunc);
    }

    private void saveButFunc(View v) {
        try {
            if (!workoutNameEditText.getText().toString().equals("")) {
                List<Exercise> updatedExercises = new ArrayList<>();
                for (Exercise exercise : exerciseEditTexts.keySet()) {
                    Map<String, EditText> editTexts = exerciseEditTexts.get(exercise);
                    assert editTexts != null;
                    Exercise updatedExercise = new Exercise(Objects.requireNonNull(editTexts.get("name")).getText().toString(),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("sets")).getText().toString()),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("reps")).getText().toString()),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("restTime")).getText().toString()));
                    updatedExercises.add(updatedExercise);
                }
                Workout updatedWorkout = new Workout(workoutNameEditText.getText().toString(), updatedExercises);
                updatedWorkout.setId(currentWorkout.getId());
                WorkoutDBHandler.updateWorkout(updatedWorkout);
            } else {
                sendWarning("Workout name empty!");
            }
        } catch (Exception e) {
            sendWarning("Input not valid!");
        }
    }

    private void sendWarning(String message) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
        Toast.makeText(EditWorkout.this, message, Toast.LENGTH_SHORT).show();
    }
}