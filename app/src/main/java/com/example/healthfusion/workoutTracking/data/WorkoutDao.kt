package com.example.healthfusion.workoutTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workout_table")
    suspend fun getAllExercises(): List<Workout>

    @Query("SELECT * FROM workout_table WHERE type = :type")
    suspend fun getExercisesByType(type: WorkoutType): List<Workout>
}