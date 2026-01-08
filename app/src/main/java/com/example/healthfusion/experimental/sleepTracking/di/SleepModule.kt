package com.example.healthfusion.experimental.sleepTracking.di

import com.example.healthfusion.healthFusionData.AppDatabase
import com.example.healthfusion.experimental.sleepTracking.data.SleepDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SleepModule {

    @Provides
    @Singleton
    fun provideSleepDao(database: AppDatabase): SleepDao {
        return database.sleepDao()
    }
}