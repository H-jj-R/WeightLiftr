package com.example.weightliftr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Random;

// TODO: Improve comments in all code
// TODO: Improve variable names and code structure
// TODO: Style every activity better
// TODO: (If time) Improve XML for handling of other devices

/**
 * Activity used as a menu screen, which links all activities.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        Button newWorkoutBut = findViewById(R.id.newWorkoutBut);
        Button editWorkoutBut = findViewById(R.id.editWorkoutBut);
        Button allWorkoutsBut = findViewById(R.id.allWorkoutsBut);
        Button startWorkoutBut = findViewById(R.id.startWorkoutBut);
        Button suggestionBut1 = findViewById(R.id.suggestionBut1);
        Button suggestionBut2 = findViewById(R.id.suggestionBut2);

        setButtonClickEvent(newWorkoutBut, AddNewWorkout.class);
        setButtonClickEvent(editWorkoutBut, EditWorkout.class);
        setButtonClickEvent(allWorkoutsBut, ViewAllWorkouts.class);
        setButtonClickEvent(startWorkoutBut, StartWorkout.class);

        String[] activityNames = { (String) newWorkoutBut.getText(),
                                         (String) editWorkoutBut.getText(),
                                         (String) allWorkoutsBut.getText() };
        Class<?>[] activityClasses = { AddNewWorkout.class,
                                             EditWorkout.class,
                                             ViewAllWorkouts.class };

        int randomIndex1 = new Random().nextInt(activityClasses.length);
        suggestionBut1.setText(getString(R.string.suggestion_text, activityNames[randomIndex1]));
        setButtonClickEvent(suggestionBut1, activityClasses[randomIndex1]);

        int randomIndex2;
        do {
            randomIndex2 = new Random().nextInt(activityClasses.length);
        } while (randomIndex2 == randomIndex1);
        suggestionBut2.setText(getString(R.string.suggestion_text, activityNames[randomIndex2]));
        setButtonClickEvent(suggestionBut2, activityClasses[randomIndex2]);
    }

    private void setButtonClickEvent(@NonNull Button button, Class<?> nextActivityClass) {
        button.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, nextActivityClass))
        );
    }
}