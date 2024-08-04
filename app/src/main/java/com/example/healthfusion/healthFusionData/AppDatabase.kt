package com.example.healthfusion.healthFusionData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDao
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao

@Database(entities = [Workout::class, Diet::class, Sleep::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun dietDao(): DietDao
    abstract fun sleepDao(): SleepDao
}