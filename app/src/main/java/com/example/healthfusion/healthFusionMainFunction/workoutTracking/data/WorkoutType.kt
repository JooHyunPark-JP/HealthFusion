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
        fields = listOf(FieldInfo.DURATION, FieldInfo.DISTANCE, FieldInfo.CALORIES_BURNED)
    ),
    CYCLING(
        "Cycling",
        R.drawable.cycling_pose,
        WorkoutType.AEROBIC,
        fields = listOf(FieldInfo.DURATION, FieldInfo.DISTANCE, FieldInfo.CALORIES_BURNED)
    ),
    WALKING(
        "Walking",
        R.drawable.walking_pose,
        WorkoutType.AEROBIC,
        fields = listOf(FieldInfo.DURATION, FieldInfo.DISTANCE, FieldInfo.CALORIES_BURNED)
    ),
    SWIMMING(
        "Swimming",
        R.drawable.swimming_pose,
        WorkoutType.AEROBIC,
        fields = listOf(FieldInfo.DURATION, FieldInfo.DISTANCE, FieldInfo.CALORIES_BURNED)
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
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS)
    ),
    SQUAT(
        "Squat",
        R.drawable.squat_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.WEIGHTS)
    ),

    DONKEY_KICK(
        "Donkey_Kick",
        R.drawable.donkey_kick_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS)
    ),

    BICEP_CURLS(
        "Bicep_Curls",
        R.drawable.bicep_curl_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.WEIGHTS)
    ),
    PLANK(
        "Plank",
        R.drawable.plank_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.DURATION)
    ),
    DEAD_LIFT(
        "Dead_Lift",
        R.drawable.dead_lift_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.WEIGHTS)
    ),

    BENCH_PRESS(
        "Bench_Press",
        R.drawable.bench_press_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(FieldInfo.SETS, FieldInfo.REPETITIONS, FieldInfo.WEIGHTS)
    ),


}

