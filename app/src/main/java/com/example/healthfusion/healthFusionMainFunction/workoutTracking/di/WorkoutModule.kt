package com.example.healthfusion.healthFusionMainFunction.workoutTracking.di

import com.example.healthfusion.healthFusionData.AppDatabase
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalDetailsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WorkoutModule {

    @Provides
    fun provideWorkoutDao(database: AppDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Provides
    fun provideWorkoutGoalDao(database: AppDatabase): WorkoutGoalDao {
        return database.workoutGoalDao()
    }

    @Provides
    fun provideWorkoutGoalDetailsDao(database: AppDatabase): WorkoutGoalDetailsDao {
        return database.workoutGoalDetailsDao()
    }
}