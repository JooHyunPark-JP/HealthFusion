package com.example.healthfusion.healthFusionMainFunction.sleepTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_table")
data class Sleep(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val date: String = "", // YYYY-MM-DD
    val startTime: String = "", // HH:MM
    val endTime: String = "", // HH:MM
    val quality: Int = 0, // 1-5 or good/bad/excellent... Haven't decided it yet.
    val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) {
    constructor() : this(
        id = 0,
        userId = "",
        date = "",
        startTime = "",
        endTime = "",
        quality = 0,
        isSynced = false
    )
}