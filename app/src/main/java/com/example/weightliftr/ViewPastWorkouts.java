package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;

public class ViewPastWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_workout);
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        Button newWorkoutBut = (Button) findViewById(R.id.backBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(ViewPastWorkouts.this, MainActivity.class))
        );

        List<String> names = new ArrayList<>();
        List<List<Exercise>> exerciseListList = new ArrayList<>();
        List<Workout> workouts = DBHandler.getAllWorkouts();
        for (Workout w : workouts) {
            names.add(w.getName());
            exerciseListList.add(w.getExercises());
        }

        // Get a reference to the LinearLayout
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // TODO: Figure out why this doesn't work
        // Inflate the views for each item and add them to the LinearLayout
        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.read_only_list_item, null);
            TextView itemTitle = view.findViewById(R.id.itemTitle);
            itemTitle.setText(names.get(i));
            List<Exercise> exercises = exerciseListList.get(i);
            linearLayout.addView(view);

            // Set an OnClickListener for each item
            view.setOnClickListener(v -> {
                View extraDetails = v.findViewById(R.id.extraDetails);
                TextView extraDetailsTextView = findViewById(R.id.extraDetails);
                extraDetailsTextView.setText("");
                TextView itemTitle1 = v.findViewById(R.id.itemTitle);

                Toast.makeText(ViewPastWorkouts.this, itemTitle1.getText(), Toast.LENGTH_SHORT).show();

                // Toggle the visibility of the extra details for the clicked item
                if (extraDetails.getVisibility() == View.GONE) {
                    for (int j = 0; j < exercises.size(); j++) {
                        extraDetailsTextView.setText((String) extraDetailsTextView.getText() + exercises.get(j).getName() + "\n");
                    }
                    extraDetails.setVisibility(View.VISIBLE);
                } else {
                    extraDetailsTextView.setText("");
                    extraDetails.setVisibility(View.GONE);
                }
            });
        }
    }
}