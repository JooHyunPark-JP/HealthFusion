package com.example.healthfusion.healthFusionMainFunction.dietTracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_table")
data class Diet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val name: String,
    val calories: Int
)