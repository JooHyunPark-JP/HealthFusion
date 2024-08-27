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
    val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
){
    constructor() : this(
        id = 0,
        name = "",
        duration = 0,
        caloriesBurned = 0,
        type = WorkoutType.AEROBIC,
        userId = "",
        isSynced = false,
        lastModified = System.currentTimeMillis()
    )
}

/*data class WorkoutDTO(
    val id: Int,
    val name: String,
    val duration: Int,
    val caloriesBurned: Int,
    val type: WorkoutType,
    val userId: String
){
    constructor() : this(
        id = 0,
        name = "",
        duration = 0,
        caloriesBurned = 0,
        type = WorkoutType.AEROBIC,
        userId = "",
    )
}

fun Workout.toDTO(): WorkoutDTO {
    return WorkoutDTO(
        id = this.id,
        name = this.name,
        duration = this.duration,
        caloriesBurned = this.caloriesBurned,
        type = this.type,
        userId = this.userId
    )
}*/

/*fun WorkoutDTO.toEntity(): Workout {
    return Workout(
        id = this.id,
        name = this.name,
        duration = this.duration,
        caloriesBurned = this.caloriesBurned,
        type = this.type,
        userId = this.userId,
        isSynced = true, // 또는 필요한 상태로 설정
        lastModified = System.currentTimeMillis()
    )
}*/


