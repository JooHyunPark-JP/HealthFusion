package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_goal_table")
data class WorkoutGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String = "",
    var isCompleted: Boolean = false,
    val userId: String = "",
    val type: WorkoutGoalType,


    val workoutName: String = "", // Syncing with WorkoutGoalDetails
    val goalFrequency: Int = 0, // Syncing with WorkoutGoalDetails
    val currentProgress: Int = 0 // Syncing with WorkoutGoalDetails
    // val createdAt: Long = System.currentTimeMillis()

) {
    constructor() : this(
        id = 0,
        text = "",
        isCompleted = false,
        userId = "",
        type = WorkoutGoalType.DAILY
    )
}


