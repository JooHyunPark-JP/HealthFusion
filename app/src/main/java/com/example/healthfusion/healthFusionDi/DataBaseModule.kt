package com.example.healthfusion.healthFusionDi

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.healthfusion.healthFusionData.AppDatabase
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
        ).addMigrations(MIGRATION_1_2).build()
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add userID field for each table
        db.execSQL("ALTER TABLE workout_table ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE sleep_table ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE diet_table ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
    }
}