package com.example.healthfusion.healthFusionMainFunction.dietTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DietDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diet: Diet): Long

    @Delete
    suspend fun delete(diet: Diet)

    @Update
    suspend fun update(diet: Diet)

    @Query("SELECT * FROM diet_table")
    fun getAllDiets(): Flow<List<Diet>>

    @Query("SELECT * FROM diet_table WHERE userId = :userId")
    fun getAllDietsForUser(userId: String): Flow<List<Diet>>

    @Query("SELECT * FROM diet_table WHERE userId = :userId")
    fun getDietForUser(userId: String): Flow<List<Diet>>

    @Query("SELECT * FROM diet_table WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedDiets(userId: String): List<Diet>

}