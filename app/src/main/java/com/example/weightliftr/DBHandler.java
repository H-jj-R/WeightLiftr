package com.example.weightliftr;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workouts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_WORKOUTS = "workouts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EXERCISES = "exercises";

    private Gson gson;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        gson = new Gson();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_WORKOUTS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EXERCISES + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        onCreate(db);
    }

    public long insertWorkout(Workout workout) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, workout.getName());
        String exercisesJson = gson.toJson(workout.getExercises());
        values.put(COLUMN_EXERCISES, exercisesJson);
        long id = db.insert(TABLE_WORKOUTS, null, values);
        workout.setId(id);
        db.close();
        return id;
    }
    public List<Workout> getAllWorkouts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS, null, null, null, null, null, null);
        List<Workout> workouts = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String exercisesJson = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISES));
            Type type = new TypeToken<List<Exercise>>() {}.getType();
            List<Exercise> exercises = gson.fromJson(exercisesJson, type);
            Workout workout = new Workout(name, exercises);
            workout.setId(id);
            workouts.add(workout);
        }
        cursor.close();
        db.close();
        return workouts;
    }
}