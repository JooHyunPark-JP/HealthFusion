package com.example.healthfusion.dietTracking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DietDao {
    @Insert
    suspend fun insert(diet: Diet)

    @Query("SELECT * FROM diet_table")
    fun getAllDiets(): Flow<List<Diet>>
}