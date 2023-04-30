package com.example.weightliftr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightliftr.objects.Workout;

import java.util.List;
import java.util.Objects;

// TODO: Figure out why this activity is broken

/**
 * Activity which allows the user to begin one of the workouts they've created.
 */
public class StartWorkout extends AppCompatActivity {

    private TextView exerciseTextView;
    private TextView setsTextView;
    private TextView repsTextView;
    private TextView setTimerTextView;
    private LinearLayout linearLayout;

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
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        WorkoutDBHandler workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v ->
                startActivity(new Intent(StartWorkout.this, MainActivity.class))
        );

        linearLayout = findViewById(R.id.startLinearLayout);
        workouts = workoutDBHandler.getAllWorkouts();

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_action_list_item, linearLayout, false);

            TextView workoutName = view.findViewById(R.id.workoutName);
            ImageButton startBut = view.findViewById(R.id.startBut);

            workoutName.setText(workouts.get(i).getName());
            startBut.setId(i);
            startBut.setImageResource(R.drawable.ic_play_circle_outline);
            startBut.setOnClickListener(this::startButFunc);

            linearLayout.addView(view);
        }
    }

    private void startButFunc(@NonNull View v) {
        linearLayout.removeAllViews();
        View newView = getLayoutInflater().inflate(R.layout.workout_in_progress, linearLayout, false);
        linearLayout.addView(newView);
        currentWorkout = workouts.get(v.getId());

        TextView titleTextView = newView.findViewById(R.id.titleTextView);
        exerciseTextView = newView.findViewById(R.id.exerciseTextView);
        setsTextView = newView.findViewById(R.id.setsTextView);
        repsTextView = newView.findViewById(R.id.repsTextView);
        Button startSetBut = newView.findViewById(R.id.startSetBut);
        Button finishSetBut = newView.findViewById(R.id.finishSetBut);
        setTimerTextView = newView.findViewById(R.id.setTimerTextView);

        titleTextView.setText(currentWorkout.getName());
        currentExerciseNum = 0;
        totalExercises = currentWorkout.getExercises().size();
        exerciseTextView.setText(currentWorkout.getExercises().get(currentExerciseNum).getName());
        currentSetNum = 1;
        totalSets = currentWorkout.getExercises().get(currentExerciseNum).getSets();
        setsTextView.setText(getString(R.string.current_set_num, currentSetNum, totalSets));
        totalReps = currentWorkout.getExercises().get(currentExerciseNum).getReps();
        repsTextView.setText(String.valueOf(totalReps));
        restTime = currentWorkout.getExercises().get(currentExerciseNum).getRestTime();
        setTimerTextView.setText(R.string.start_next_set);

        countDownTimer = new CountDownTimer(restTime * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secsLeft = (int) (millisUntilFinished / 1000);
                setTimerTextView.setText(String.valueOf(secsLeft));
            }
            @Override
            public void onFinish() {
                setTimerTextView.setText(R.string.start_next_set);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(300);
                }
            }
        };

        startSetBut.setOnClickListener(this::startSetFunc);
        finishSetBut.setOnClickListener(this::finishSetFunc);
    }

    private void startSetFunc(View v) {
        if (setTimerTextView.getText() != getText(R.string.set_in_progress)) {
            countDownTimer.cancel();
            setTimerTextView.setText(R.string.set_in_progress);
        }
    }

    private void finishSetFunc(View v) {
        if (setTimerTextView.getText() == getText(R.string.set_in_progress)) {
            currentSetNum++;
            if (currentSetNum > totalSets) {
                currentExerciseNum++;
                currentSetNum = 1;
                if (currentExerciseNum > totalExercises) {
                    finish();
                    startActivity(getIntent());
                }
            }
            exerciseTextView.setText(currentWorkout.getExercises().get(currentExerciseNum).getName());
            totalSets = currentWorkout.getExercises().get(currentExerciseNum).getSets();
            setsTextView.setText(getString(R.string.current_set_num, currentSetNum, totalSets));
            totalReps = currentWorkout.getExercises().get(currentExerciseNum).getReps();
            repsTextView.setText(String.valueOf(totalReps));
            restTime = currentWorkout.getExercises().get(currentExerciseNum).getRestTime();
            countDownTimer.start();
        } else {
            Toast.makeText(StartWorkout.this, "Start a set first!", Toast.LENGTH_SHORT).show();
        }
    }
}