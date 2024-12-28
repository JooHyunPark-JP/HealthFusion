package com.example.healthfusion.healthFusionMainFunction.dietTracking.data

import kotlinx.serialization.Serializable


data class FoodSearchResponse(
    val products: List<Product> = emptyList()
)


data class Product(
    val product_name: String? = null,
    val nutriments: Nutriments? = null
)


data class Nutriments(
    val energy_kcal: Float? = null,
    val proteins: Float? = null,
    val carbohydrates: Float? = null,
    val fat: Float? = null
)