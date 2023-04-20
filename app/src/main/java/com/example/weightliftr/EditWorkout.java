package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class EditWorkout extends AppCompatActivity {

    private Button backBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        backBut = findViewById(R.id.backBut);
        backBut.setOnClickListener(event ->
                startActivity(new Intent(EditWorkout.this, MainActivity.class))
        );

        // TODO: Show current workouts, and allow user to add and remove exercises, and edit properties

    }
}