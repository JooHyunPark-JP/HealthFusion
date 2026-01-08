package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import com.example.healthfusion.R

enum class WorkoutType {
    AEROBIC, ANAEROBIC
}


enum class AerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType,
    val fields: List<FieldInfo>, //User input fields (create workout screen)
    val graphFields: List<FieldInfo> = fields // Fields showing on graph (Workout History Screen)
) {
    RUNNING(
        "Running",
        R.drawable.running_pose2,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        ),
        graphFields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        )
    ),
    CYCLING(
        "Cycling",
        R.drawable.cycling_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        ),
        graphFields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        )
    ),
    WALKING(
        "Walking",
        R.drawable.walking_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        ),
        graphFields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        )
    ),
    SWIMMING(
        "Swimming",
        R.drawable.swimming_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,

            ),
        graphFields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
        )
    ),
    JUMPING_ROPE(
        "Jumping_Rope",
        R.drawable.jumping_rope_pose,
        WorkoutType.AEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.CALORIES_BURNED),
        graphFields = listOf(
            FieldInfo.REPETITIONS,
            FieldInfo.CALORIES_BURNED,
        )
    )
}

enum class AnaerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType,
    val fields: List<FieldInfo>, //User input fields (create workout screen)
    val graphMetrics: List<AnaerobicStrengthMetric> // Fields showing on graph (Workout History Screen)
) {
    PUSH_UP(
        "Push_Up",
        R.drawable.pushup_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )

    ),
    SQUAT(
        "Squat",
        R.drawable.squat_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

    DONKEY_KICK(
        "Donkey_Kick",
        R.drawable.donkey_kick_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

    BICEP_CURLS(
        "Bicep_Curls",
        R.drawable.bicep_curl_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),
    PLANK(
        "Plank",
        R.drawable.plank_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.DURATION,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.DURATION
        )
    ),

    HAND_GRIPPER(
        "Hand_Gripper",
        R.drawable.hand_grippler_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

    SHOULDER_PRESS(
        "Shoulder_Press",
        R.drawable.shoulder_press_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

    DUMBBELL_LOW(
        "Dumbbell_Low",
        R.drawable.dumbbell_row_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

    DEAD_LIFT(
        "Dead_Lift",
        R.drawable.dead_lift_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

    BENCH_PRESS(
        "Bench_Press",
        R.drawable.bench_press_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.TIMER,
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
        ),
        graphMetrics = listOf(
            AnaerobicStrengthMetric.TOTAL_VOLUME,
            AnaerobicStrengthMetric.TOTAL_SETS,
            AnaerobicStrengthMetric.TOTAL_REPS
        )
    ),

}

