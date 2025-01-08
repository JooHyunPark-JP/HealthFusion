package com.example.healthfusion.healthFusionMainFunction.dietTracking.di

import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.FoodSearchResponse
import com.example.healthfusion.healthFusionMainFunction.dietTracking.network.OpenFoodFactsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DietRepository @Inject constructor(private val api: OpenFoodFactsApi) {

    suspend fun searchFood(query: String): List<Diet> {
        return try {
            val response: FoodSearchResponse = api.searchFood(query)
            response.products.map { product ->
                Diet(
                    name = product.productName ?: "Unknown",
                    calories = product.nutriments?.energyKcal?.toInt() ?: 0,
                    userId = "",
                    isSynced = false,
                    lastModified = System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}