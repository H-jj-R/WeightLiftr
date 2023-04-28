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

public class ViewAllWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_workouts);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
       WorkoutDBHandler workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(ViewAllWorkouts.this, MainActivity.class))
        );

        List<Workout> workouts = workoutDBHandler.getAllWorkouts();

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.read_only_list_item, null);
            List<Exercise> exercises = workouts.get(i).getExercises();
            TextView itemTitle = view.findViewById(R.id.workoutNameTextView);
            itemTitle.setText(workouts.get(i).getName());
            linearLayout.addView(view);

            view.setOnClickListener(v ->
                    clickViewFunc(v, exercises)
            );
        }
    }

    private void clickViewFunc(@NonNull View v, List<Exercise> exercises) {
        @SuppressLint("CutPasteId") View extraDetails = v.findViewById(R.id.exercisesTextView);
        @SuppressLint("CutPasteId") TextView extraDetailsTextView = v.findViewById(R.id.exercisesTextView);

        // Toggle the visibility of the extra details for the clicked item
        if (extraDetails.getVisibility() == View.GONE) {
            for (int j = 0; j < exercises.size(); j++) {
                extraDetailsTextView.setText(extraDetailsTextView.getText() + exercises.get(j).getName() + "\n");
            }
            extraDetails.setVisibility(View.VISIBLE);

        } else {
            extraDetailsTextView.setText("");
            extraDetails.setVisibility(View.GONE);
        }
    }

}