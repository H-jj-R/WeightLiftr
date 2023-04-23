package com.example.weightliftr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weightliftr.objects.Workout;

import java.util.List;
import java.util.Objects;

public class EditWorkout extends AppCompatActivity {

    private Button backBut;
    private WorkoutDBHandler WorkoutDBHandler;

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

        // TODO: Show current workouts, and allow user to add and remove exercises, and edit properties
        // TODO: Use same methodology as with StartWorkout

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        List<Workout> workouts = WorkoutDBHandler.getAllWorkouts();

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.edit_workout_list_item, linearLayout, false);

            TextView workoutName = view.findViewById(R.id.workoutName);
            ImageButton startBut = view.findViewById(R.id.startBut);

            workoutName.setText(workouts.get(i).getName());
            startBut.setId(i);

            startBut.setOnClickListener(v -> {
                linearLayout.removeAllViews();
                View newView = getLayoutInflater().inflate(R.layout.edit_workout_details, linearLayout, false);
                linearLayout.addView(newView);

                currentWorkout = workouts.get(v.getId());
                TextView tv = newView.findViewById(R.id.titleTextView);
                tv.setText(currentWorkout.getName());

                LinearLayout verticalLayout = newView.findViewById(R.id.verticalLayout);
                View newNewView = getLayoutInflater().inflate(R.layout.edit_workout_exercises, verticalLayout, false);
                verticalLayout.addView(newNewView);

                //TODO: Figure out how to add edit_workout_exercises to ScrollView in edit_workout_details

            });
            linearLayout.addView(view);
        }
    }
}