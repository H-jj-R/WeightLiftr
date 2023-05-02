package com.example.weightliftr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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

/**
 * Activity which allows the user to begin one of the workouts they've created.
 */
public class StartWorkout extends AppCompatActivity {

    private TextView exerciseTextView;
    private TextView setsTextView;
    private TextView repsTextView;
    private TextView setTimerTextView;
    private LinearLayout startLinearLayout;

    private CountDownTimer restTimer;
    private List<Workout> allWorkouts;
    private Workout currentWorkout;
    private int currentExerciseNum;
    private int currentSetNum;
    private NotificationCompat.Builder notificationBuilder;
    private boolean isNotificationShown;

    private int totalExercises;
    private int totalSets;
    private int totalReps;
    private int restTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set base activity details and information
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start_workout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        WorkoutDBHandler workoutDBHandler = new WorkoutDBHandler(this.getApplicationContext());
        createNotification();

        Button backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(v -> {
            if (restTimer != null) restTimer.cancel();
            startActivity(new Intent(StartWorkout.this, MainActivity.class));
        });

        allWorkouts = workoutDBHandler.getAllWorkouts();

        // Display all workout options from LinearLayout
        startLinearLayout = findViewById(R.id.startLinearLayout);
        for (int i = 0; i < allWorkouts.size(); i++) {
            View selectableView = LayoutInflater.from(this)
                    .inflate(R.layout.start_action_list_item, startLinearLayout, false);

            TextView workoutName = selectableView.findViewById(R.id.workoutName);
            ImageButton startBut = selectableView.findViewById(R.id.startBut);

            workoutName.setText(allWorkouts.get(i).getName());
            startBut.setId(i);
            startBut.setImageResource(R.drawable.ic_play_circle_outline);
            startBut.setOnClickListener(this::startButFunc);

            startLinearLayout.addView(selectableView);
        }
    }

    private void startButFunc(@NonNull View v) {
        // Once a workout has been selected, clear screen and show new View (from same LinearLayout)
        startLinearLayout.removeAllViews();
        View startView = getLayoutInflater().inflate(R.layout.workout_in_progress, startLinearLayout, false);
        startLinearLayout.addView(startView);
        currentWorkout = allWorkouts.get(v.getId());

        TextView titleTextView = startView.findViewById(R.id.titleTextView);
        exerciseTextView = startView.findViewById(R.id.exerciseTextView);
        setsTextView = startView.findViewById(R.id.setsTextView);
        repsTextView = startView.findViewById(R.id.repsTextView);
        Button startSetBut = startView.findViewById(R.id.startSetBut);
        Button finishSetBut = startView.findViewById(R.id.finishSetBut);
        setTimerTextView = startView.findViewById(R.id.setTimerTextView);

        // Set initial relevant details in TextViews
        titleTextView.setText(currentWorkout.getName());
        currentExerciseNum = 0;
        totalExercises = currentWorkout.getExercises().size();
        exerciseTextView.setText(currentWorkout.getExercises().get(currentExerciseNum).getName());
        currentSetNum = 1;
        totalSets = currentWorkout.getExercises().get(currentExerciseNum).getSets();
        setsTextView.setText(getString(R.string.current_set_num, currentSetNum, totalSets));
        totalReps = currentWorkout.getExercises().get(currentExerciseNum).getReps();
        repsTextView.setText(getString(R.string.current_reps_num, totalReps));
        restTime = currentWorkout.getExercises().get(currentExerciseNum).getRestTime();
        setTimerTextView.setText(R.string.start_next_set);

        createTimer();
        startSetBut.setOnClickListener(this::startSetFunc);
        finishSetBut.setOnClickListener(this::finishSetFunc);
    }

    private void startSetFunc(View v) {
        // User has notified they have started their set
        if (setTimerTextView.getText() != getText(R.string.set_in_progress)) {
            restTimer.cancel();
            setTimerTextView.setText(R.string.set_in_progress);
        }
    }

    private void finishSetFunc(View v) {
        // User finishes their current set
        if (setTimerTextView.getText() == getText(R.string.set_in_progress)) {
            currentSetNum++;
            // If no more sets, next exercise
            if (currentSetNum > totalSets) {
                currentExerciseNum++;
                currentSetNum = 1;
                // If no more exercises, reset activity state
                if (currentExerciseNum > totalExercises) {
                    finish();
                    startActivity(getIntent());
                }
            }
            // Update displayed exercise details
            exerciseTextView.setText(currentWorkout.getExercises().get(currentExerciseNum).getName());
            totalSets = currentWorkout.getExercises().get(currentExerciseNum).getSets();
            setsTextView.setText(getString(R.string.current_set_num, currentSetNum, totalSets));
            totalReps = currentWorkout.getExercises().get(currentExerciseNum).getReps();
            repsTextView.setText(getString(R.string.current_reps_num, totalReps));
            restTime = currentWorkout.getExercises().get(currentExerciseNum).getRestTime();
            restTimer.start();
        } else {
            Toast.makeText(StartWorkout.this, "Start a set first!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTimer() {
        // Set a countdown timer based on the exercises given rest time
        restTimer = new CountDownTimer(restTime * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secsLeft = (int) (millisUntilFinished / 1000);
                setTimerTextView.setText(String.valueOf(secsLeft));
            }
            @Override
            public void onFinish() {
                // Notify user their next set should start now, using a Toast and vibration,
                // or if the app is not in the foreground, using a notification
                showNotification();
                setTimerTextView.setText(R.string.start_next_set);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(300);
                }
            }
        };
    }

    private void createNotification() {
        // Define notification channel - required to send notifications in API >= 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_primary", "Primary Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification itself
        notificationBuilder = new NotificationCompat.Builder(this, "channel_primary")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Time for your next set!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        isNotificationShown = false;
    }

    private void showNotification() {
        // Show the notification only if it's not already shown and the app is in the background
        if (!hasWindowFocus() && !isNotificationShown) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, notificationBuilder.build());
            isNotificationShown = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When the app is brought back to the foreground, cancel the notification
        if (isNotificationShown) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.cancel(1);
            isNotificationShown = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the app is terminated, cancel the notification
        if (isNotificationShown) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.cancel(1);
            isNotificationShown = false;
        }
    }
}