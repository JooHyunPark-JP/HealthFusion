package com.example.healthfusion.workoutTracking.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Workout::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): WorkoutDao
}