package com.example.healthfusion

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthfusion.dietTracking.data.Diet
import com.example.healthfusion.dietTracking.data.DietDao
import com.example.healthfusion.workoutTracking.data.Workout
import com.example.healthfusion.workoutTracking.data.WorkoutDao

@Database(entities = [Workout::class, Diet::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun dietDao(): DietDao
}