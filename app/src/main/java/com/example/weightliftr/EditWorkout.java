package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weightliftr.objects.Workout;

import java.util.List;

public class EditWorkout extends AppCompatActivity {

    private Button backBut;
    private DBHandler DBHandler;

    private Workout currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        DBHandler = new DBHandler(this.getApplicationContext());

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(event ->
                startActivity(new Intent(EditWorkout.this, MainActivity.class))
        );

        // TODO: Show current workouts, and allow user to add and remove exercises, and edit properties
        // TODO: Use same methodology as with StartWorkout

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        List<Workout> workouts = DBHandler.getAllWorkouts();

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

                LinearLayout verticalLayout = newView.findViewById(R.id.verticalLayout);

                //for (int j = 0; j < workouts.size(); j++) {

                    TextView tv = new TextView(this);
                    tv.setText("testView");
                    tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    verticalLayout.addView(tv);

                //}
            });
            linearLayout.addView(view);
        }
    }
}