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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_workouts);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        WorkoutDBHandler workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(ViewAllWorkouts.this, MainActivity.class))
        );

        List<Workout> workouts = workoutDBHandler.getAllWorkouts();

        LinearLayout linearLayout = findViewById(R.id.startLinearLayout);
        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.read_only_list_item, linearLayout, false);
            exercises = workouts.get(i).getExercises();
            TextView itemTitle = view.findViewById(R.id.workoutNameTextView);
            itemTitle.setText(workouts.get(i).getName());
            linearLayout.addView(view);

            view.setTag(workouts.get(i));
            view.setOnClickListener(this::clickViewFunc);
        }
    }

    private void clickViewFunc(@NonNull View v) {
        @SuppressLint("CutPasteId") View extraDetailsView = v.findViewById(R.id.exercisesTextView);
        @SuppressLint("CutPasteId") TextView extraDetailsTextView = v.findViewById(R.id.exercisesTextView);

        if (extraDetailsView.getVisibility() == View.GONE) {
            for (int i = 0; i < exercises.size(); i++) {
                List<Exercise> exercises = ((Workout) v.getTag()).getExercises();
                 extraDetailsTextView.setText(
                         getString(R.string.extra_workout_details, extraDetailsTextView.getText(),
                                 exercises.get(i).getName(), exercises.get(i).getSets(), exercises.get(i).getReps(),
                                 exercises.get(i).getRestTime()));
            }
            extraDetailsView.setVisibility(View.VISIBLE);

        } else {
            extraDetailsTextView.setText("");
            extraDetailsView.setVisibility(View.GONE);
        }
    }
}