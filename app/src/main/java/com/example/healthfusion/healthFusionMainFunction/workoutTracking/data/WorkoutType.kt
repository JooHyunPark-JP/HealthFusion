package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import com.example.healthfusion.R

enum class WorkoutType {
    AEROBIC, ANAEROBIC
}

/*enum class AerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType
) {
    RUNNING("Running", R.drawable.running_pose2, workoutType = WorkoutType.AEROBIC),
    CYCLING("Cycling", R.drawable.cycling_pose, workoutType = WorkoutType.AEROBIC),
    WALKING("Walking", R.drawable.walking_pose, workoutType = WorkoutType.AEROBIC),
    SWIMMING("Swimming", R.drawable.swimming_pose, workoutType = WorkoutType.AEROBIC),
    JUMPING_ROPE("Jumping_Rope", R.drawable.jumping_rope_pose, workoutType = WorkoutType.AEROBIC)
}

enum class AnaerobicWorkout(
    val workoutName: String,
    val imageResource: Int,
    val workoutType: WorkoutType
) {
    PUSH_UP("Push_Up", R.drawable.pushup_pose, workoutType = WorkoutType.ANAEROBIC),
    SQUAT("Squat", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
    BENCH_PRESS("Bench_Press", R.drawable.squat_pose, workoutType = WorkoutType.ANAEROBIC),
    BICEP_CURLS("Bicep_Curls", R.drawable.bicep_curl_pose, workoutType = WorkoutType.ANAEROBIC),
    PLANK("Plank", R.drawable.plank_pose, workoutType = WorkoutType.ANAEROBIC),
    DEAD_LIFT("Dead_Lift", R.drawable.dead_lift_pose, workoutType = WorkoutType.ANAEROBIC),
}*/

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
            FieldInfo("Duration (minutes)", FieldType.TEXT),
            FieldInfo("Distance (km)", FieldType.TEXT),
            FieldInfo("Calories Burned", FieldType.TEXT)
        )
    ),
    CYCLING(
        "Cycling",
        R.drawable.cycling_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo("Duration (minutes)", FieldType.TEXT),
            FieldInfo("Distance (km)", FieldType.TEXT),
            FieldInfo("Calories Burned", FieldType.TEXT)
        )
    ),
    WALKING(
        "Walking",
        R.drawable.walking_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo("Duration (minutes)", FieldType.TEXT),
            FieldInfo("Distance (km)", FieldType.TEXT),
            FieldInfo("Calories Burned", FieldType.TEXT)
        )
    ),
    SWIMMING(
        "Swimming",
        R.drawable.swimming_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo("Duration (minutes)", FieldType.TEXT),
            FieldInfo("Distance (km)", FieldType.TEXT),
            FieldInfo("Calories Burned", FieldType.TEXT)
        )
    ),
    JUMPING_ROPE(
        "Jumping_Rope",
        R.drawable.jumping_rope_pose,
        WorkoutType.AEROBIC,
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Repetitions", FieldType.TEXT),
            FieldInfo("Calories Burned", FieldType.TEXT)
        )
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
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Repetitions", FieldType.TEXT)
        )
    ),
    SQUAT(
        "Squat",
        R.drawable.squat_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Repetitions", FieldType.TEXT),
            FieldInfo("Weights (kg)", FieldType.TEXT)
        )
    ),
    BENCH_PRESS(
        "Bench_Press",
        R.drawable.squat_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Repetitions", FieldType.TEXT),
            FieldInfo("Weights (kg)", FieldType.TEXT)
        )
    ),
    BICEP_CURLS(
        "Bicep_Curls",
        R.drawable.bicep_curl_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Repetitions", FieldType.TEXT),
            FieldInfo("Weights (kg)", FieldType.TEXT)
        )
    ),
    PLANK(
        "Plank",
        R.drawable.plank_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Duration (seconds)", FieldType.TEXT)
        )
    ),
    DEAD_LIFT(
        "Dead_Lift",
        R.drawable.dead_lift_pose,
        WorkoutType.ANAEROBIC,
        fields = listOf(
            FieldInfo("Sets", FieldType.TEXT),
            FieldInfo("Repetitions", FieldType.TEXT),
            FieldInfo("Weights (kg)", FieldType.TEXT)
        )
    )
}

enum class FieldType {
    TEXT // expand later: TIMER, UNIT_SELECTION (km,m) etc
}

data class FieldInfo(
    val label: String,
    val fieldType: FieldType
)