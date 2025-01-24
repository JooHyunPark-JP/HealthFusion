package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import com.example.healthfusion.R

enum class WorkoutType {
    AEROBIC, ANAEROBIC
}


enum class AerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType,
    val fields: List<FieldInfo>
) {
    RUNNING(
        "Running",
        R.drawable.running_pose2,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
            FieldInfo.TIMER
        )
    ),
    CYCLING(
        "Cycling",
        R.drawable.cycling_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
            FieldInfo.TIMER
        )
    ),
    WALKING(
        "Walking",
        R.drawable.walking_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
            FieldInfo.TIMER
        )
    ),
    SWIMMING(
        "Swimming",
        R.drawable.swimming_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo.DURATION,
            FieldInfo.DISTANCE,
            FieldInfo.CALORIES_BURNED,
            FieldInfo.TIMER
        )
    ),
    JUMPING_ROPE(
        "Jumping_Rope",
        R.drawable.jumping_rope_pose,
        WorkoutType.AEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.CALORIES_BURNED)
    )
}

enum class AnaerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType,
    val fields: List<FieldInfo>
) {
    PUSH_UP(
        "Push_Up",
        R.drawable.pushup_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.TIMER)
    ),
    SQUAT(
        "Squat",
        R.drawable.squat_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.TIMER
        )
    ),

    DONKEY_KICK(
        "Donkey_Kick",
        R.drawable.donkey_kick_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.TIMER)
    ),

    BICEP_CURLS(
        "Bicep_Curls",
        R.drawable.bicep_curl_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
            FieldInfo.TIMER
        )
    ),
    PLANK(
        "Plank",
        R.drawable.plank_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.DURATION, FieldInfo.TIMER, FieldInfo.TIMER)
    ),

    HAND_GRIPPER(
        "Hand_Gripper",
        R.drawable.hand_grippler_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.WEIGHTS, FieldInfo.TIMER)
    ),

    SHOULDER_PRESS(
        "Shoulder_Press",
        R.drawable.shoulder_press_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
            FieldInfo.TIMER
        )
    ),

    DUMBBELL_LOW(
        "Dumbbell_Low",
        R.drawable.dumbbell_row_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
            FieldInfo.TIMER
        )
    ),

    DEAD_LIFT(
        "Dead_Lift",
        R.drawable.dead_lift_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
            FieldInfo.TIMER
        )
    ),

    BENCH_PRESS(
        "Bench_Press",
        R.drawable.bench_press_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo.SETS,
            FieldInfo.REPETITIONS,
            FieldInfo.WEIGHTS,
            FieldInfo.EQUIPMENT_TYPE,
            FieldInfo.GRIP_STYLE,
            FieldInfo.TIMER
        )
    ),

}

