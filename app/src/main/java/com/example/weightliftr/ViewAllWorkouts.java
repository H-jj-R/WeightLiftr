package com.example.weightliftr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAllWorkouts extends AppCompatActivity {

    private DBHandler DBHandler;

    private Button backBut;
    private TextView itemTitle;

    private List<Workout> workouts;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_workouts);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        DBHandler = new DBHandler(this.getApplicationContext());

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(ViewAllWorkouts.this, MainActivity.class))
        );

        workouts = DBHandler.getAllWorkouts();

        linearLayout = findViewById(R.id.linearLayout);

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.read_only_list_item, null);
            List<Exercise> exercises = workouts.get(i).getExercises();
            itemTitle = view.findViewById(R.id.itemTitle);
            itemTitle.setText(workouts.get(i).getName());
            linearLayout.addView(view);

            view.setOnClickListener(v ->
                    clickViewFunc(v, exercises)
            );
        }
    }

    private void clickViewFunc(View v, List<Exercise> exercises) {
        @SuppressLint("CutPasteId") View extraDetails = v.findViewById(R.id.extraDetails);
        @SuppressLint("CutPasteId") TextView extraDetailsTextView = v.findViewById(R.id.extraDetails);

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