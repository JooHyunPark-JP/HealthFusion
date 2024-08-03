package com.example.healthfusion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM exercise_table")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise_table WHERE type = :type")
    suspend fun getExercisesByType(type: ExerciseType): List<Exercise>
}