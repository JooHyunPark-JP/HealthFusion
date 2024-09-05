package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

data class WorkoutGoal(
    val text: String,
    var isCompleted: Boolean = false

) {
    constructor() : this(
        text = "",
        isCompleted = false
    )
}


