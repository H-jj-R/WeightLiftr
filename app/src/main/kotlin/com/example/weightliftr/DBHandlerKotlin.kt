package com.example.weightliftr

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.weightliftr.objects.Exercise
import com.example.weightliftr.objects.Workout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DBHandlerKotlin(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val gson = Gson()

    companion object {
        private const val DATABASE_NAME = "workouts.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_WORKOUTS = "workouts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EXERCISES = "exercises"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_WORKOUTS " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EXERCISES TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUTS")
        onCreate(db)
    }

    fun insertWorkout(workout: Workout) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, workout.name)
        val exercisesJson = gson.toJson(workout.exercises)
        values.put(COLUMN_EXERCISES, exercisesJson)
        val id = db.insert(TABLE_WORKOUTS, null, values)
        workout.id = id
        db.close()
    }

    fun updateWorkout(workout: Workout) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, workout.name)
        val exercisesJson = gson.toJson(workout.exercises)
        values.put(COLUMN_EXERCISES, exercisesJson)
        val args = arrayOf(workout.id.toString())
        db.update(TABLE_WORKOUTS, values, "$COLUMN_ID=?", args)
        db.close()
    }

    fun removeWorkout(workout: Workout) {
        val db = writableDatabase
        val args = arrayOf(workout.id.toString())
        db.delete(TABLE_WORKOUTS, "$COLUMN_ID=?", args)
        db.close()
    }

    fun getAllWorkouts(): List<Workout> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_WORKOUTS, null, null,
                                        null, null, null, null)
        val workouts: MutableList<Workout> = mutableListOf()
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            @SuppressLint("Range") val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            @SuppressLint("Range") val exercisesJson = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISES))
            val type: Type = object : TypeToken<List<Exercise?>?>() {}.type
            val exercises: List<Exercise> = gson.fromJson(exercisesJson, type)
            val workout = Workout(name, exercises)
            workout.id = id
            workouts.add(workout)
        }
        cursor.close()
        db.close()
        return workouts
    }
}