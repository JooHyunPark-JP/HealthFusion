package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val duration: Int = 0,
    val caloriesBurned: Int = 0,
    val type: WorkoutType = WorkoutType.AEROBIC,
    val userId: String = "",
    val isSynced: Boolean = false
){
    constructor() : this(
        id = 0,
        name = "",
        duration = 0,
        caloriesBurned = 0,
        type = WorkoutType.AEROBIC,
        userId = "",
        isSynced = false
    )
}

