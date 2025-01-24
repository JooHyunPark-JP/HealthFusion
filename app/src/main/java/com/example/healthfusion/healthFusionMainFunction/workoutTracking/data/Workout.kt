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
    val type: WorkoutType = WorkoutType.AEROBIC,

    //Aerobic properties
    val duration: Int? = null,
    val caloriesBurned: Int? = null,
    val distance: Int? = null,

    //Anaerobic properties
    val set: Int? = null,
    val repetition: Int? = null,
    val weight: Int? = null,
    val equipmentType : String? = null,
    val gripStyle : String? = null,


    val userId: String = "",
    val workoutDate: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) {
    constructor() : this(
        id = 0,
        name = "",
        type = WorkoutType.AEROBIC,

        duration = null,
        caloriesBurned = null,
        distance = null,

        set = null,
        repetition = null,
        weight = null,
        equipmentType = null,
        gripStyle = null,

        userId = "",
        workoutDate = System.currentTimeMillis(),
        isSynced = false,
        lastModified = System.currentTimeMillis()
    )
}

data class WorkoutDTO(
    val id: Int,
    val name: String,
    val type: WorkoutType,

    val duration: Int?,
    val caloriesBurned: Int?,
    val distance: Int?,

    val set: Int?,
    val repetition: Int?,
    val weight: Int?,
    val equipmentType : String? = null,
    val gripStyle : String? = null,


    val userId: String,
    val workoutDate: String,
    val lastModified: String
) {
    constructor() : this(
        id = 0,
        name = "",
        type = WorkoutType.AEROBIC,


        duration = null,
        caloriesBurned = null,
        distance = null,

        set = null,
        repetition = null,
        weight = null,
        equipmentType = null,
        gripStyle = null,


        userId = "",
        workoutDate = "",
        lastModified = ""
    )
}

fun Workout.toDTO(dateFormatter: DateFormatter): WorkoutDTO {
    return WorkoutDTO(
        id = this.id,
        name = this.name,
        type = this.type,

        duration = this.duration,
        caloriesBurned = this.caloriesBurned,
        distance = this.distance,

        set = this.set,
        repetition = this.repetition,
        weight = this.weight,
        equipmentType = this.equipmentType,
        gripStyle = this.gripStyle,

        userId = this.userId,
        workoutDate = dateFormatter.formatMillisToDateTime(this.workoutDate),
        lastModified = dateFormatter.formatMillisToDateTime(this.lastModified)
    )
}

fun WorkoutDTO.toEntity(dateFormatter: DateFormatter): Workout {
    val lastModifiedMillis =
        dateFormatter.parseDateTimeToMillis(this.lastModified) ?: System.currentTimeMillis()
    return Workout(
        id = this.id,
        name = this.name,
        type = this.type,

        duration = this.duration,
        caloriesBurned = this.caloriesBurned,
        distance = this.distance,

        set = this.set,
        repetition = this.repetition,
        weight = this.weight,
        equipmentType = this.equipmentType,
        gripStyle = this.gripStyle,

        userId = this.userId,
        workoutDate = dateFormatter.parseDateTimeToMillis(this.workoutDate)
            ?: System.currentTimeMillis(),
        isSynced = true,
        lastModified = lastModifiedMillis
    )
}


