package com.example.healthfusion.healthFusionMainFunction.dietTracking.network

import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.FoodSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class OpenFoodFactsApi(private val client: HttpClient) {

    // call api function
    suspend fun searchFood(query: String): FoodSearchResponse {
        return client.get("https://world.openfoodfacts.org/cgi/search.pl") {
            url {
                parameters.append("search_terms", query) // search term
                parameters.append("search_simple", "1") // simple search mode
                parameters.append("json", "1")
                parameters.append("fields", "product_name,nutriments,categories")// json response
            }
        }.body() // convert json to foodsearchresponse
    }
}