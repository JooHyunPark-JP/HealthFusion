package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import com.example.healthfusion.R

enum class WorkoutType {
    AEROBIC, ANAEROBIC
}

enum class AerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType
) {
    RUNNING("Running", R.drawable.running_pose2, workoutType = WorkoutType.AEROBIC),
    CYCLING("Cycling", R.drawable.cycling_pose, workoutType = WorkoutType.AEROBIC),
    WALKING("Walking", R.drawable.walking_pose, workoutType = WorkoutType.AEROBIC),
    /*SWIMMING("Swimming", R.drawable.running_pose2, workoutType = WorkoutType.AEROBIC),
    JUMPING_ROPE("Jumping_Rope", R.drawable.running_pose2, workoutType = WorkoutType.AEROBIC)*/
}

enum class AnaerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType
) {
    PUSHUPS("PushUps", R.drawable.pushup_pose, workoutType = WorkoutType.ANAEROBIC),
    SQUATS("Squats", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
/*    BENCH_PRESS("Bench_Press", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
    BICEP_CURLS("Bicep_Curls", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
    PLANK("Plank", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
    DEAD_LIFT("Dead_lift", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
    HIP_UP("HIP_UP", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC)*/


}