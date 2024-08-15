package com.example.healthfusion.healthFusionMainFunction.dietTracking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import kotlinx.coroutines.flow.Flow

@Dao
interface DietDao {
    @Insert
    suspend fun insert(diet: Diet)

    @Query("SELECT * FROM diet_table")
    fun getAllDiets(): Flow<List<Diet>>

    @Query("SELECT * FROM diet_table WHERE userId = :userId")
    fun getAllDietsForUser(userId: String): Flow<List<Diet>>

    @Query("SELECT * FROM diet_table WHERE userId = :userId")
    fun getDietForUser(userId: String): Flow<List<Diet>>

}