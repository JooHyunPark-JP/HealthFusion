package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthfusion.util.DateFormatter


//Workout data class for room database
//WorkoutDTO data for firestore

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
) {
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

//workout for image control for gridviews and such
data class WorkOutName(
    val name: String,
    val imageResource: Int
)

data class WorkoutDTO(
    val id: Int,
    val name: String,
    val duration: Int,
    val caloriesBurned: Int,
    val type: WorkoutType,
    val userId: String,
    val lastModified: String
) {
    constructor() : this(
        id = 0,
        name = "",
        duration = 0,
        caloriesBurned = 0,
        type = WorkoutType.AEROBIC,
        userId = "",
        lastModified = ""
    )
}

fun Workout.toDTO(dateFormatter: DateFormatter): WorkoutDTO {
    return WorkoutDTO(
        id = this.id,
        name = this.name,
        duration = this.duration,
        caloriesBurned = this.caloriesBurned,
        type = this.type,
        userId = this.userId,
        lastModified = dateFormatter.formatMillisToDateTime(this.lastModified)
    )
}

fun WorkoutDTO.toEntity(dateFormatter: DateFormatter): Workout {
    val lastModifiedMillis =
        dateFormatter.parseDateTimeToMillis(this.lastModified) ?: System.currentTimeMillis()
    return Workout(
        id = this.id,
        name = this.name,
        duration = this.duration,
        caloriesBurned = this.caloriesBurned,
        type = this.type,
        userId = this.userId,
        isSynced = true,
        lastModified = lastModifiedMillis
    )
}


