package com.example.healthfusion.healthFusionMainFunction.dietTracking.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FoodSearchResponse(
    val products: List<Product> = emptyList()
)

@Serializable
data class Product(
    @SerialName("product_name")
    val productName: String? = null,
    val nutriments: Nutriments? = null
)

@Serializable
data class Nutriments(
    @SerialName("energy-kcal")
    val energyKcal: Float? = null,
    val proteins: Float? = null,
    val carbohydrates: Float? = null,
    val fat: Float? = null
)