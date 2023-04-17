package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;

public class StartWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        Button newWorkoutBut = (Button) findViewById(R.id.backBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(StartWorkout.this, MainActivity.class))
        );

        //TODO: Display workouts, and when one is selected, display all relevant information
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        List<String> names = new ArrayList<>();
        List<List<Exercise>> exerciseListList = new ArrayList<>();
        List<Workout> workouts = DBHandler.getAllWorkouts();
        for (Workout w : workouts) {
            names.add(w.getName());
            exerciseListList.add(w.getExercises());
        }

        for (String name : names) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_workout_list_item, linearLayout, false);

            TextView textView = view.findViewById(R.id.item_text);
            ImageButton button = view.findViewById(R.id.item_button);

            textView.setText(name);

            button.setOnClickListener(v -> {
                // Handle button click event here
            });

            linearLayout.addView(view);
        }
    }
}