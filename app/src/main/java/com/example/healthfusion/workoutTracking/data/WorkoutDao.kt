package com.example.healthfusion.workoutTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workout_table")
    fun getAllExercises(): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE type = :type")
    fun getExercisesByType(type: WorkoutType): Flow<List<Workout>>
}