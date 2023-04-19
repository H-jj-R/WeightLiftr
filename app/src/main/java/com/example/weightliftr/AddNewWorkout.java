package com.example.weightliftr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weightliftr.objects.Exercise;
import com.example.weightliftr.objects.Workout;

import java.util.ArrayList;
import java.util.List;

public class AddNewWorkout extends AppCompatActivity {

    List<Exercise> exercisesToAdd = new ArrayList<>();
    List<String> mItems = new ArrayList<>();
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);
        DBHandler DBHandler = new DBHandler(this.getApplicationContext());

        Button newWorkoutBut = (Button) findViewById(R.id.backBut);
        newWorkoutBut.setOnClickListener(event ->
                startActivity(new Intent(AddNewWorkout.this, MainActivity.class))
        );

        Button mShowEditTextButton = findViewById(R.id.newExBut);
        LinearLayout mEditTextLayout = findViewById(R.id.edit_text_layout);
        EditText mEditText1 = findViewById(R.id.edit_text_1);
        EditText mEditText2 = findViewById(R.id.edit_text_2);
        EditText mEditText3 = findViewById(R.id.edit_text_3);
        Button mSaveButton = findViewById(R.id.save_button);
        ListView mListView = findViewById(R.id.list_view);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(mAdapter);

        mShowEditTextButton.setOnClickListener(event -> {
            if (mEditTextLayout.getVisibility() == View.GONE) {
                mEditTextLayout.setVisibility(View.VISIBLE);
                mShowEditTextButton.setText("Hide EditTexts");
            } else {
                mEditTextLayout.setVisibility(View.GONE);
                mShowEditTextButton.setText("Show EditTexts");
            }
        });

        mSaveButton.setOnClickListener(event -> {
            String text = mEditText1.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                mItems.add(text);
                mAdapter.notifyDataSetChanged();
            }

            mEditText1.getText().clear();
            mEditText2.getText().clear();
            mEditText3.getText().clear();

            mEditTextLayout.setVisibility(View.GONE);
            mShowEditTextButton.setText("Show EditTexts");
        });
    }
}
