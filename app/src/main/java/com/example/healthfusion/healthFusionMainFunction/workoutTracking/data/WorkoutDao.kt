package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout): Long

    @Delete
    suspend fun delete(workout: Workout)

    @Update
    suspend fun update(workout: Workout)

    @Query("SELECT * FROM workout_table")
    fun getAllExercises(): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE type = :type")
    fun getExercisesByType(type: WorkoutType): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE userId = :userId AND name = :workoutName")
    fun getWorkoutsByName(userId: String, workoutName: String): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE userId = :userId")
    fun getWorkoutsForUser(userId: String): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedWorkouts(userId: String): List<Workout>

    @Query("SELECT * FROM workout_table WHERE id = :id")
    suspend fun getWorkoutById(id: Int): Workout?

    @Query(
        """
    SELECT * FROM workout_table 
    WHERE userId = :userId AND workoutDate BETWEEN :startOfWeek AND :endOfWeek
"""
    )
    fun getWorkoutsByDateRange(
        userId: String,
        startOfWeek: Long,
        endOfWeek: Long
    ): Flow<List<Workout>>

}