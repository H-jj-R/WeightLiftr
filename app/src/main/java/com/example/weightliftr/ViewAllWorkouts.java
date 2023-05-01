package com.example.weightliftr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.List;
import java.util.Objects;

/**
 * Activity which allows the user to quickly and easily view the details of all workouts they've created.
 */
public class ViewAllWorkouts extends AppCompatActivity {

    List<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set base activity details and information
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_view_all_workouts);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        WorkoutDBHandler workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(ViewAllWorkouts.this, MainActivity.class))
        );

        List<Workout> allWorkouts = workoutDBHandler.getAllWorkouts();

        // Add every workout to the LinearLayout
        LinearLayout startLinearLayout = findViewById(R.id.startLinearLayout);
        for (int i = 0; i < allWorkouts.size(); i++) {
            View workoutView = LayoutInflater.from(this)
                    .inflate(R.layout.read_only_list_item, startLinearLayout, false);
            exercises = allWorkouts.get(i).getExercises();
            TextView itemTitle = workoutView.findViewById(R.id.workoutNameTextView);
            itemTitle.setText(allWorkouts.get(i).getName());
            startLinearLayout.addView(workoutView);

            workoutView.setTag(allWorkouts.get(i));
            workoutView.setOnClickListener(this::clickViewFunc);
        }
    }

    // If a workout is clicked on, display all extra details for that workout
    private void clickViewFunc(@NonNull View v) {
        @SuppressLint("CutPasteId") View exercisesView = v.findViewById(R.id.exercisesTextView);
        @SuppressLint("CutPasteId") TextView exercisesTextView = v.findViewById(R.id.exercisesTextView);

        if (exercisesView.getVisibility() == View.GONE) {
            for (int i = 0; i < exercises.size(); i++) {
                List<Exercise> exercises = ((Workout) v.getTag()).getExercises();
                exercisesTextView.setText(getString(R.string.extra_workout_details, exercisesTextView.getText(),
                                 exercises.get(i).getName(), exercises.get(i).getSets(), exercises.get(i).getReps(),
                                 exercises.get(i).getRestTime()));
            }
            exercisesView.setVisibility(View.VISIBLE);

        } else {
            exercisesTextView.setText("");
            exercisesView.setVisibility(View.GONE);
        }
    }
}