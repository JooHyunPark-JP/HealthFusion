package com.example.healthfusion.healthFusionMainFunction.dietTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthfusion.util.DateFormatter

@Entity(tableName = "diet_table")
data class Diet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val name: String = "",
    val calories: Int = 0,
    val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) {
    constructor() : this(
        id = 0,
        userId = "",
        name = "",
        calories = 0,
        isSynced = false
    )
}

data class DietDTO(
    val id: Int,
    val userId: String,
    val name: String,
    val calories: Int,
    val lastModified: String
) {
    constructor() : this(
        id = 0,
        userId = "",
        name = "",
        calories = 0,
        lastModified = ""
    )
}

fun Diet.toDTO(dateFormatter: DateFormatter): DietDTO {
    return DietDTO(
        id = this.id,
        userId = this.userId,
        name = this.name,
        calories = this.calories,
        lastModified = dateFormatter.formatMillisToDateTime(this.lastModified)
    )
}

fun DietDTO.toEntity(dateFormatter: DateFormatter): Diet {
    val lastModifiedMillis =
        dateFormatter.parseDateTimeToMillis(this.lastModified) ?: System.currentTimeMillis()
    return Diet(
        id = this.id,
        userId = this.userId,
        name = this.name,
        calories = this.calories,
        isSynced = true,
        lastModified = lastModifiedMillis
    )
}