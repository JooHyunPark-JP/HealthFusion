package com.example.healthfusion.healthFusionMainFunction.dietTracking.di

import com.example.healthfusion.healthFusionMainFunction.dietTracking.network.ApiClient
import com.example.healthfusion.healthFusionMainFunction.dietTracking.network.OpenFoodFactsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): ApiClient {
        return ApiClient
    }

    @Provides
    @Singleton
    fun provideOpenFoodFactsApi(apiClient: ApiClient): OpenFoodFactsApi {
        return OpenFoodFactsApi(apiClient.httpClient)
    }
}
