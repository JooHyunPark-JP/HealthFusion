package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutGoalDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workoutGoalDetails: WorkoutGoalDetails): Long

    @Update
    suspend fun update(workoutGoalDetails: WorkoutGoalDetails)

    @Query("SELECT * FROM workout_goal_details WHERE userId = :userId AND workoutName = :workoutName")
    fun getWorkoutGoalDetails(userId: String, workoutName: String): Flow<List<WorkoutGoalDetails>>

    @Query("SELECT * FROM workout_goal_details WHERE userId = :userId AND goalPeriod = :goalPeriod")
    fun getGoalsByPeriod(userId: String, goalPeriod: String): Flow<List<WorkoutGoalDetails>>

    @Query(
        """
    SELECT * FROM workout_goal_details 
    WHERE userId = :userId AND goalPeriod = :goalPeriod 
    AND :startOfWeek <= createdAt AND createdAt <= :endOfWeek
"""
    )
    fun getGoalsByPeriodAndTimeRange(
        userId: String,
        goalPeriod: String,
        startOfWeek: Long,
        endOfWeek: Long
    ): Flow<List<WorkoutGoalDetails>>


    @Delete
    suspend fun delete(workoutGoalDetails: WorkoutGoalDetails)
}