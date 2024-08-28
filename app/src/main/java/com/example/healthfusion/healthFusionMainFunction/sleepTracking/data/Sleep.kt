package com.example.healthfusion.healthFusionMainFunction.sleepTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthfusion.util.DateFormatter

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

data class SleepDTO(
    val id: Int = 0,
    val userId: String = "",
    val date: String = "", // YYYY-MM-DD
    val startTime: String = "", // HH:MM
    val endTime: String = "", // HH:MM
    val quality: Int = 0, // 1-5 or good/bad/excellent... Haven't decided it yet.
    val lastModified: String = "" // formatted date-time string
) {
    constructor() : this(
        id = 0,
        userId = "",
        date = "",
        startTime = "",
        endTime = "",
        quality = 0,
        lastModified = ""
    )
}

fun Sleep.toDTO(dateFormatter: DateFormatter): SleepDTO {
    return SleepDTO(
        id = this.id,
        userId = this.userId,
        date = this.date,
        startTime = this.startTime,
        endTime = this.endTime,
        quality = this.quality,
        lastModified = dateFormatter.formatMillisToDateTime(this.lastModified)
    )
}

fun SleepDTO.toEntity(dateFormatter: DateFormatter): Sleep {
    val lastModifiedMillis =
        dateFormatter.parseDateTimeToMillis(this.lastModified) ?: System.currentTimeMillis()
    return Sleep(
        id = this.id,
        userId = this.userId,
        date = this.date,
        startTime = this.startTime,
        endTime = this.endTime,
        quality = this.quality,
        isSynced = true,
        lastModified = lastModifiedMillis
    )
}