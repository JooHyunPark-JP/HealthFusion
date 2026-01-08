package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

enum class FieldType {
    TEXT, TIMER, TIMEPICKER, SEGMENTED // expand later: UNIT_SELECTION (km,m) etc
}

enum class FieldInfo(val label: String, val type: FieldType) {
    DURATION("Duration", FieldType.TIMEPICKER),
    DISTANCE("Distance (km)", FieldType.TEXT),
    CALORIES_BURNED("Calories Burned", FieldType.TEXT),
    SETS("Sets", FieldType.TEXT),
    REPETITIONS("Repetitions", FieldType.TEXT),
    WEIGHTS("Weights (kg)", FieldType.TEXT),
    TIMER("Timer", FieldType.TIMER),
    EQUIPMENT_TYPE("Equipment Type", FieldType.SEGMENTED), // New field
    GRIP_STYLE("Grip Style", FieldType.SEGMENTED) // New field
}

fun Workout.getFieldValue(field: FieldInfo): Double {
    return when (field) {
        FieldInfo.CALORIES_BURNED -> this.caloriesBurned?.toDouble() ?: 0.0
        FieldInfo.DURATION -> this.duration?.toDouble() ?: 0.0
        FieldInfo.DISTANCE -> this.distance?.toDouble() ?: 0.0
        FieldInfo.SETS -> this.set?.toDouble() ?: 0.0
        FieldInfo.REPETITIONS -> this.repetition?.toDouble() ?: 0.0
        FieldInfo.WEIGHTS -> this.weight?.toDouble() ?: 0.0
        FieldInfo.TIMER -> 0.0
        FieldInfo.EQUIPMENT_TYPE -> 0.0
        FieldInfo.GRIP_STYLE -> 0.0

    }
}

//For now it's only for jumping rope (Aerobic)
fun Workout.getGraphValue(fieldInfo: FieldInfo): Double {
    return when (fieldInfo) {
        FieldInfo.REPETITIONS -> {
            val isJumpingRope =
                this.name == AerobicWorkout.JUMPING_ROPE.workoutName &&
                        this.type == WorkoutType.AEROBIC

            if (isJumpingRope) {
                AnaerobicStrengthMetric.TOTAL_REPS.valueFor(this)
            } else {
                getFieldValue(fieldInfo)
            }
        }

        else -> {
            getFieldValue(fieldInfo)
        }
    }
}