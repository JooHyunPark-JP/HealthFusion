package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data


/**
 * - TOTAL_VOLUME: Sum of set (weight * reps)
 * - TOP_WEIGHT: Heaviest Weight
 * - TOTAL_REPS:
 * - TOTAL_SETS: Number of set
 */
enum class AnaerobicStrengthMetric(
    val label: String, // chip or dropdown
    val shortLabel: String, //short name, (for chip + label)
    val unitLabel: String // y-axis in the graph
) {
    TOTAL_VOLUME(
        label = "Total Volume",
        shortLabel = "Volume (Sets * Reps * Weight)",
        unitLabel = "kg·reps"
    ),
    TOP_WEIGHT(
        label = "Top Weight",
        shortLabel = "Top Wt",
        unitLabel = "kg"
    ),
    TOTAL_REPS(
        label = "Total Reps",
        shortLabel = "Total Repetition (Sets * Reps)",
        unitLabel = "reps"
    ),
    TOTAL_SETS(
        label = "Total Sets",
        shortLabel = "Sets",
        unitLabel = "sets"
    ),

    DURATION(
        label = "Duration",
        shortLabel = "Duration",
        unitLabel = "Duration"
    )
}

fun AnaerobicStrengthMetric.valueFor(workout: Workout): Double {
    val sets = workout.getFieldValue(FieldInfo.SETS)
    val reps = workout.getFieldValue(FieldInfo.REPETITIONS)
    val weight = workout.getFieldValue(FieldInfo.WEIGHTS)
    val duration = workout.getFieldValue(FieldInfo.DURATION)

    return when (this) {
        AnaerobicStrengthMetric.TOTAL_VOLUME -> {
            // Total volume: sets * reps * weight
            sets * reps * weight
        }

        AnaerobicStrengthMetric.TOP_WEIGHT -> {
            weight
        }

        AnaerobicStrengthMetric.TOTAL_REPS -> {
            // total rep = set * repetition
            sets * reps
        }

        AnaerobicStrengthMetric.TOTAL_SETS -> {
            // number of sets
            sets
        }

        AnaerobicStrengthMetric.DURATION -> {
            duration
        }
    }
}
