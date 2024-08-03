package com.example.healthfusion.workoutTracking.di

import android.content.Context
import androidx.room.Room
import com.example.healthfusion.workoutTracking.data.AppDatabase
import com.example.healthfusion.workoutTracking.data.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "healthfusion_database"
        ).build()
    }

    @Provides
    fun provideExerciseDao(database: AppDatabase): WorkoutDao {
        return database.exerciseDao()
    }
}