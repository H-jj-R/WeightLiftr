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

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ViewPastWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_workout);

        Button newWorkoutBut = (Button) findViewById(R.id.backBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(ViewPastWorkouts.this, MainActivity.class))
        );

        ArrayList<String> items = new ArrayList<>();
        // Add some dummy items to the list
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        items.add("Item 5");

        // Get a reference to the LinearLayout
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // Inflate the views for each item and add them to the LinearLayout
        for (int i = 0; i < items.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.list_item, null);
            TextView textView = view.findViewById(R.id.itemTitle);
            textView.setText(items.get(i));
            linearLayout.addView(view);

            // Set an OnClickListener for each item
            view.setOnClickListener(v -> {
                // Get the TextView inside the clicked view
                TextView textView1 = v.findViewById(R.id.itemTitle);

                // Show a Toast message with the text of the clicked item
                Toast.makeText(ViewPastWorkouts.this, textView1.getText(), Toast.LENGTH_SHORT).show();

                // Toggle the visibility of the extra details for the clicked item
                View extraDetails = v.findViewById(R.id.extraDetails);
                if (extraDetails.getVisibility() == View.GONE) {
                    extraDetails.setVisibility(View.VISIBLE);
                } else {
                    extraDetails.setVisibility(View.GONE);
                }
            });
        }
    }
}