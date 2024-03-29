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

    private LinearLayout editLinearLayout;
    private EditText workoutNameEditText;

    private List<Workout> allWorkouts;
    private Map<Exercise, Map<String, EditText>> exerciseEditTextsMap;
    private Workout currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set base activity details and information
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_edit_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(EditWorkout.this, MainActivity.class))
        );

        allWorkouts = workoutDBHandler.getAllWorkouts();

        // Display all workout options from LinearLayout
        editLinearLayout = findViewById(R.id.editLinearLayout);
        for (int i = 0; i < allWorkouts.size(); i++) {
            View selectableView = LayoutInflater.from(this)
                    .inflate(R.layout.start_action_list_item, editLinearLayout, false);

            TextView workoutName = selectableView.findViewById(R.id.workoutName);
            ImageButton startBut = selectableView.findViewById(R.id.startBut);

            workoutName.setText(allWorkouts.get(i).getName());
            startBut.setImageResource(R.drawable.ic_baseline_edit);
            startBut.setId(i);

            startBut.setOnClickListener(this::startButFunc);
            editLinearLayout.addView(selectableView);
        }
    }

    private void startButFunc(@NonNull View v) {
        // Once a workout has been selected, clear screen and show new View (from same LinearLayout)
        editLinearLayout.removeAllViews();
        View baseEditView = getLayoutInflater().inflate(R.layout.edit_workout_details, editLinearLayout, false);
        editLinearLayout.addView(baseEditView);

        currentWorkout = allWorkouts.get(v.getId());
        workoutNameEditText = baseEditView.findViewById(R.id.workoutNameEditText);
        workoutNameEditText.setText(currentWorkout.getName());
        LinearLayout verticalLayout = baseEditView.findViewById(R.id.verticalLayout);
        exerciseEditTextsMap = new HashMap<>();

        // Expand View again to show EditTexts for each exercise in workout
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

            // Save EditDetails with corresponding content in Map
            // So that all correct exercises updated if details change
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
        // Attempt to update details, ensuring they are valid
        // Try-catch used for Integer.parseInt(), so it only works if inputs are valid numbers
        try {
            if (!workoutNameEditText.getText().toString().equals("")) {
                List<Exercise> updatedExercises = new ArrayList<>();

                // Get each individual exercise details from Map and save to ArrayList
                for (Exercise exercise : exerciseEditTextsMap.keySet()) {
                    Map<String, EditText> editTexts = exerciseEditTextsMap.get(exercise);
                    assert editTexts != null;
                    Exercise updatedExercise = new Exercise(Objects.requireNonNull(editTexts.get("name")).getText().toString(),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("sets")).getText().toString()),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("reps")).getText().toString()),
                            Integer.parseInt(Objects.requireNonNull(editTexts.get("restTime")).getText().toString()));
                    updatedExercises.add(updatedExercise);
                }

                // Make new Workout and set ID to same as selected workout to overwrite in database
                Workout updatedWorkout = new Workout(workoutNameEditText.getText().toString(), updatedExercises);
                updatedWorkout.setId(currentWorkout.getId());
                workoutDBHandler.updateWorkout(updatedWorkout);
                sendToast("Changes saved.");

                // Restart activity
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
        // Restart activity
        this.finish();
        startActivity(getIntent());
    }

    private void deleteWorkoutButFunc(@NonNull View v) {
        workoutDBHandler.removeWorkout(currentWorkout);
        sendToast("Workout deleted.");
        // Restart activity
        this.finish();
        startActivity(getIntent());
    }

    private void sendToast(String message) {
        // Send a toast, along with a vibration to notify them of a action they are attempting to perform / have performed
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
        Toast.makeText(EditWorkout.this, message, Toast.LENGTH_SHORT).show();
    }
}