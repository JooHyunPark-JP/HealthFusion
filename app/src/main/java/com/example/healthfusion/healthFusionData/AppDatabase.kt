package com.example.healthfusion.healthFusionData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDao
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalDao

@Database(entities = [Workout::class, Diet::class, Sleep::class, WorkoutGoal::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun dietDao(): DietDao
    abstract fun sleepDao(): SleepDao
    abstract fun workoutGoalDao(): WorkoutGoalDao
}