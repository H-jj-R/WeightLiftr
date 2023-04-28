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

/**
 * Activity which allows user to edit the details of an existing workout.
 */
public class EditWorkout extends AppCompatActivity {

    private WorkoutDBHandler workoutDBHandler;

    private LinearLayout linearLayout;
    private EditText workoutNameEditText;

    private List<Workout> allWorkouts;
    private Map<Exercise, Map<String, EditText>> exerciseEditTextsMap;
    private Workout currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

//        List<Exercise> exercises= new ArrayList<>();
//        exercises.add(new Exercise("Quad Pull", 3, 10, 60));
//        exercises.add(new Exercise("Squat", 3, 10, 60));
//        exercises.add(new Exercise("Calf Raise", 3, 15, 60));
//
//        Workout w = new Workout("Leg Workout", exercises);
//        workoutDBHandler.insertWorkout(w);
//
//        List<Exercise> exercises1 = new ArrayList<>();
//        exercises1.add(new Exercise("Bicep Curls", 4, 10, 35));
//        exercises1.add(new Exercise("Tricep Pulls", 4, 10, 35));
//        exercises1.add(new Exercise("Forearm Stretches", 3, 15, 35));
//
//        Workout w1 = new Workout("Ind. Muscle Workout", exercises1);
//        workoutDBHandler.insertWorkout(w1);
//
//        List<Exercise> exercises2 = new ArrayList<>();
//        exercises2.add(new Exercise("Bench Press", 3, 10, 110));
//        exercises2.add(new Exercise("Seated Row", 3, 10, 110));
//        exercises2.add(new Exercise("Lat Pull", 2, 15, 110));
//
//        Workout w2 = new Workout("Upper Workout", exercises2);
//        workoutDBHandler.insertWorkout(w2);

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(EditWorkout.this, MainActivity.class))
        );

        linearLayout = findViewById(R.id.linearLayout);

        allWorkouts = workoutDBHandler.getAllWorkouts();

        for (int i = 0; i < allWorkouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_workout_list_item, linearLayout, false);

            TextView workoutName = view.findViewById(R.id.workoutName);
            ImageButton startBut = view.findViewById(R.id.startBut);

            workoutName.setText(allWorkouts.get(i).getName());
            startBut.setImageResource(R.drawable.ic_baseline_edit);
            startBut.setId(i);

            startBut.setOnClickListener(this::startButFunc);
            linearLayout.addView(view);
        }
    }

    private void startButFunc(@NonNull View v) {
        linearLayout.removeAllViews();
        View baseEditView = getLayoutInflater().inflate(R.layout.edit_workout_details, linearLayout, false);
        linearLayout.addView(baseEditView);

        currentWorkout = allWorkouts.get(v.getId());
        TextView tv = baseEditView.findViewById(R.id.titleTextView);
        tv.setText(currentWorkout.getName());
        workoutNameEditText = baseEditView.findViewById(R.id.workoutNameEditText);
        workoutNameEditText.setText(currentWorkout.getName());
        LinearLayout verticalLayout = baseEditView.findViewById(R.id.verticalLayout);

        exerciseEditTextsMap = new HashMap<>();

        for (int i = 0; i < currentWorkout.getExercises().size(); i++) {
            View exerciseEditView = getLayoutInflater().inflate(R.layout.edit_workout_exercises, verticalLayout, false);
            verticalLayout.addView(exerciseEditView);

            EditText nameEditText = exerciseEditView.findViewById(R.id.nameEditText);
            EditText setsEditText = exerciseEditView.findViewById(R.id.setsEditText);
            EditText repsEditText = exerciseEditView.findViewById(R.id.repsEditText);
            EditText restTimeEditText = exerciseEditView.findViewById(R.id.restTimeEditText);

            nameEditText.setText(currentWorkout.getExercises().get(i).getName());
            setsEditText.setText(String.valueOf(currentWorkout.getExercises().get(i).getSets()));
            repsEditText.setText(String.valueOf(currentWorkout.getExercises().get(i).getReps()));
            restTimeEditText.setText(String.valueOf(currentWorkout.getExercises().get(i).getRestTime()));

            Exercise exercise = new Exercise(nameEditText.getText().toString(),
                    Integer.parseInt(setsEditText.getText().toString()),
                    Integer.parseInt(repsEditText.getText().toString()),
                    Integer.parseInt(restTimeEditText.getText().toString()));

            Map<String, EditText> editTexts = new HashMap<>();
            editTexts.put("name", nameEditText);
            editTexts.put("sets", setsEditText);
            editTexts.put("reps", repsEditText);
            editTexts.put("restTime", restTimeEditText);
            exerciseEditTextsMap.put(exercise, editTexts);
        }

        Button saveBut = baseEditView.findViewById(R.id.saveBut);
        Button discardBut = baseEditView.findViewById(R.id.discardBut);
        Button deleteWorkoutBut = baseEditView.findViewById(R.id.deleteWorkoutBut);

        saveBut.setOnClickListener(this::saveButFunc);
        discardBut.setOnClickListener(this::discardButFunc);
        deleteWorkoutBut.setOnClickListener(this::deleteWorkoutButFunc);
    }

    private void saveButFunc(@NonNull View v) {
        try {
            if (!workoutNameEditText.getText().toString().equals("")) {
                List<Exercise> updatedExercises = new ArrayList<>();
                for (Exercise exercise : exerciseEditTextsMap.keySet()) {
                    Map<String, EditText> editTexts = exerciseEditTextsMap.get(exercise);
                    assert editTexts != null;
                    Exercise updatedExercise = new Exercise(Objects.requireNonNull(editTexts.get("name")).getText().toString(),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("sets")).getText().toString()),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("reps")).getText().toString()),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("restTime")).getText().toString()));
                    updatedExercises.add(updatedExercise);
                }
                Workout updatedWorkout = new Workout(workoutNameEditText.getText().toString(), updatedExercises);
                updatedWorkout.setId(currentWorkout.getId());
                workoutDBHandler.updateWorkout(updatedWorkout);
                sendToast("Changes saved.");
                this.finish();
                startActivity(getIntent());
            } else {
                sendToast("Workout name empty!");
            }
        } catch (Exception e) {
            sendToast("Input not valid!");
        }
    }

    private void discardButFunc(@NonNull View v) {
        sendToast("Changes discarded.");
        this.finish();
        startActivity(getIntent());
    }

    private void deleteWorkoutButFunc(@NonNull View v) {
        workoutDBHandler.removeWorkout(currentWorkout);
        sendToast("Workout deleted.");
        this.finish();
        startActivity(getIntent());
    }

    private void sendToast(String message) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
        Toast.makeText(EditWorkout.this, message, Toast.LENGTH_SHORT).show();
    }
}