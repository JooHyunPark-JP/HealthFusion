package com.example.healthfusion.healthFusionMainFunction.sleepTracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleep: Sleep) : Long

    @Query("SELECT * FROM sleep_table")
    fun getAllSleepRecords(): Flow<List<Sleep>>

    @Update
    suspend fun update(sleep: Sleep)

    @Delete
    suspend fun delete(sleep: Sleep)

    @Query("SELECT * FROM sleep_table WHERE userId = :userId")
    fun getAllSleepRecordsForUser(userId: String): Flow<List<Sleep>>

    @Query("SELECT * FROM sleep_table WHERE userId = :userId")
    fun getSleepForUser(userId: String): Flow<List<Sleep>>

    @Query("SELECT * FROM sleep_table WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedSleeps(userId: String): List<Sleep>

    @Query("SELECT * FROM sleep_table WHERE id = :id")
    suspend fun getSleepById(id: Int): Sleep?
}
