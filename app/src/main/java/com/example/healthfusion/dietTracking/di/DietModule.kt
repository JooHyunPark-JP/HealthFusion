package com.example.healthfusion.dietTracking.di

import com.example.healthfusion.AppDatabase
import com.example.healthfusion.dietTracking.data.DietDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DietModule {

    @Provides
    @Singleton
    fun provideDietDao(database: AppDatabase): DietDao {
        return database.dietDao()
    }
}