package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_goal_details")
data class WorkoutGoalDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workoutName: String,
 //   val workoutType: WorkoutType,
    val goalFrequency: Int,
    val goalType: WorkoutGoalType,
    val currentProgress: Int = 0,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis()
)