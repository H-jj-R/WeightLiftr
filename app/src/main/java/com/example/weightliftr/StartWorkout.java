package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.concurrent.atomic.AtomicInteger;

public class StartWorkout extends AppCompatActivity {

    private Button backBut;
    private DBHandler DBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        DBHandler = new DBHandler(this.getApplicationContext());
        backBut = findViewById(R.id.backBut);

        backBut.setOnClickListener(event ->
                startActivity(new Intent(StartWorkout.this, MainActivity.class))
        );

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        List<List<Exercise>> exerciseListList = new ArrayList<>();
        List<Workout> workouts = DBHandler.getAllWorkouts();
        for (Workout w : workouts) {
            exerciseListList.add(w.getExercises());
        }

        for (int i = 0; i < workouts.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.start_workout_list_item, linearLayout, false);

            TextView workoutName = view.findViewById(R.id.workoutName);
            ImageButton startBut = view.findViewById(R.id.startBut);

            workoutName.setText(workouts.get(i).getName());
            startBut.setId(i);

            startBut.setOnClickListener(v -> {
                linearLayout.removeAllViews();
                View newView = getLayoutInflater().inflate(R.layout.workout_in_progress, linearLayout, false);
                linearLayout.addView(newView);
                Workout currentWorkout = workouts.get(v.getId());

                TextView titleTextView = newView.findViewById(R.id.titleTextView);
                TextView exerciseTextView = newView.findViewById(R.id.exerciseTextView);
                TextView setsTextView = newView.findViewById(R.id.setsTextView);
                TextView repsTextView = newView.findViewById(R.id.repsTextView);
                Button startSetBut = newView.findViewById(R.id.startSetBut);
                Button finishSetBut = newView.findViewById(R.id.finishSetBut);
                TextView setTimerTextView = newView.findViewById(R.id.setTimerTextView);

                titleTextView.setText(currentWorkout.getName());
                AtomicInteger atomicExerciseNum = new AtomicInteger(0);
                exerciseTextView.setText(currentWorkout.getExercises().get(atomicExerciseNum.get()).getName());
                AtomicInteger atomicSetNum = new AtomicInteger(1);
                int sets = currentWorkout.getExercises().get(atomicExerciseNum.get()).getSets();
                setsTextView.setText("Sets: " + atomicSetNum + " / " + sets);
                int reps = currentWorkout.getExercises().get(atomicExerciseNum.get()).getReps();
                repsTextView.setText(String.valueOf(reps));
                int restTime = currentWorkout.getExercises().get(atomicExerciseNum.get()).getRestTime();
                setTimerTextView.setText("START NEXT SET");

                CountDownTimer countDownTimer = new CountDownTimer(restTime * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int secsLeft = (int) (millisUntilFinished / 1000);
                        setTimerTextView.setText(String.valueOf(secsLeft));
                    }
                    @Override
                    public void onFinish() {
                        setTimerTextView.setText("START NEXT SET");
                    }
                };

                startSetBut.setOnClickListener(e -> {
                    countDownTimer.cancel();
                    setTimerTextView.setText("SET IN PROGRESS");
                });

                finishSetBut.setOnClickListener(e -> {
                    if (setTimerTextView.getText() == "SET IN PROGRESS") {

                        atomicExerciseNum.getAndIncrement();
                        atomicSetNum.getAndIncrement();
                        if (atomicSetNum.get() > sets) {
                            // TODO: Next Exercise

                        }

                        countDownTimer.start();
                    } else {
                        Toast.makeText(StartWorkout.this, "Start a set first!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            linearLayout.addView(view);
        }
    }
}