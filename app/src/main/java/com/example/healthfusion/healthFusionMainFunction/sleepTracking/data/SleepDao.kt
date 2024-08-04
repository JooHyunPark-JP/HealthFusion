package com.example.healthfusion.healthFusionMainFunction.sleepTracking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Insert()
    suspend fun insert(sleep: Sleep)

    @Query("SELECT * FROM sleep_table")
    fun getAllSleepRecords(): Flow<List<Sleep>>
}
