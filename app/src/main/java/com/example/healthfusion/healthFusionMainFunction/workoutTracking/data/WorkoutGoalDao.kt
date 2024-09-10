package com.example.healthfusion.healthFusionMainFunction.workoutTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workoutGoal: WorkoutGoal): Long

    @Update
    suspend fun update(workoutGoal: WorkoutGoal)

    // 'daily' or 'weekly'
    @Query("SELECT * FROM workout_goal_table WHERE userId = :userId AND type = :goalType")
    fun getGoalsForUserAndType(userId: String, goalType: WorkoutGoalType): Flow<List<WorkoutGoal>>

    @Delete
    suspend fun delete(workoutGoal: WorkoutGoal)
}