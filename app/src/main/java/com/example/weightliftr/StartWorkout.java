package com.example.weightliftr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StartWorkout extends AppCompatActivity {

    private DBHandler DBHandler;

    private Button backBut;
    private TextView titleTextView;
    private TextView exerciseTextView;
    private TextView setsTextView;
    private TextView repsTextView;
    private Button startSetBut;
    private Button finishSetBut;
    private TextView setTimerTextView;
    private LinearLayout linearLayout;
    private TextView workoutName;
    private ImageButton startBut;

    private CountDownTimer countDownTimer;
    private List<Workout> workouts;
    private Workout currentWorkout;
    private int currentExerciseNum;
    private int currentSetNum;

    private int totalExercises;
    private int totalSets;
    private int totalReps;
    private int restTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        DBHandler = new DBHandler(this.getApplicationContext());

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(StartWorkout.this, MainActivity.class))
        );

        linearLayout = findViewById(R.id.linearLayout);
        workouts = DBHandler.getAllWorkouts();

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_workout_list_item, linearLayout, false);

            workoutName = view.findViewById(R.id.workoutName);
            startBut = view.findViewById(R.id.startBut);

            workoutName.setText(workouts.get(i).getName());
            startBut.setId(i);
            startBut.setOnClickListener(this::startButFunc);

            linearLayout.addView(view);
        }
    }

    private void startButFunc(@NonNull View v) {
        linearLayout.removeAllViews();
        View newView = getLayoutInflater().inflate(R.layout.workout_in_progress, linearLayout, false);
        linearLayout.addView(newView);
        currentWorkout = workouts.get(v.getId());

        titleTextView = newView.findViewById(R.id.titleTextView);
        exerciseTextView = newView.findViewById(R.id.exerciseTextView);
        setsTextView = newView.findViewById(R.id.setsTextView);
        repsTextView = newView.findViewById(R.id.repsTextView);
        startSetBut = newView.findViewById(R.id.startSetBut);
        finishSetBut = newView.findViewById(R.id.finishSetBut);
        setTimerTextView = newView.findViewById(R.id.setTimerTextView);

        titleTextView.setText(currentWorkout.getName());
        currentExerciseNum = 0;
        totalExercises = currentWorkout.getExercises().size();
        exerciseTextView.setText(currentWorkout.getExercises().get(currentExerciseNum).getName());
        currentSetNum = 1;
        totalSets = currentWorkout.getExercises().get(currentExerciseNum).getSets();
        setsTextView.setText("Current Set: " + currentSetNum + " / " + totalSets);
        totalReps = currentWorkout.getExercises().get(currentExerciseNum).getReps();
        repsTextView.setText(String.valueOf(totalReps));
        restTime = currentWorkout.getExercises().get(currentExerciseNum).getRestTime();
        setTimerTextView.setText("START NEXT SET");

        countDownTimer = new CountDownTimer(restTime * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secsLeft = (int) (millisUntilFinished / 1000);
                setTimerTextView.setText(String.valueOf(secsLeft));
            }
            @Override
            public void onFinish() {
                setTimerTextView.setText("START NEXT SET");
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(300);
            }
        };

        startSetBut.setOnClickListener(v1 -> startSetFunc());
        finishSetBut.setOnClickListener(v1 -> finishSetFunc());
    }

    private void startSetFunc() {
        if (setTimerTextView.getText() != "SET IN PROGRESS") {
            countDownTimer.cancel();
            setTimerTextView.setText("SET IN PROGRESS");
        }
    }

    private void finishSetFunc() {
        if (setTimerTextView.getText() == "SET IN PROGRESS") {
            currentSetNum++;
            if (currentSetNum > totalSets) {
                currentExerciseNum++;
                currentSetNum = 1;
                if (currentExerciseNum > totalExercises) {
                    startActivity(new Intent(StartWorkout.this, MainActivity.class));
                    // TODO: Something different when workout finished
                }
            }
            exerciseTextView.setText(currentWorkout.getExercises().get(currentExerciseNum).getName());
            totalSets = currentWorkout.getExercises().get(currentExerciseNum).getSets();
            setsTextView.setText("Sets: " + currentSetNum + " / " + totalSets);
            totalReps = currentWorkout.getExercises().get(currentExerciseNum).getReps();
            repsTextView.setText(String.valueOf(totalReps));
            restTime = currentWorkout.getExercises().get(currentExerciseNum).getRestTime();
            countDownTimer.start();
        } else {
            Toast.makeText(StartWorkout.this, "Start a set first!", Toast.LENGTH_SHORT).show();
        }
    }
}