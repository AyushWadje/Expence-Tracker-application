package com.example.expencetracker.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for WorkManager configuration.
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    /**
     * Provide WorkManager configuration with Hilt support.
     */
    @Singleton
    @Provides
    fun provideWorkManagerConfiguration(
        hiltWorkerFactory: HiltWorkerFactory
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
    }

    /**
     * Provide WorkManager instance.
     */
    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context,
        configuration: Configuration
    ): WorkManager {
        WorkManager.initialize(context, configuration)
        return WorkManager.getInstance(context)
    }
}

