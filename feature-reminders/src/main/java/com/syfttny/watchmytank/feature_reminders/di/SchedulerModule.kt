package com.syfttny.watchmytank.feature_reminders.di

import com.syfttny.watchmytank.domain.scheduler.ReminderScheduler
import com.syfttny.watchmytank.feature_reminders.scheduler.ReminderSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing scheduler-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class) // Install in SingletonComponent for application-wide scope
abstract class SchedulerModule {

    /**
     * Binds ReminderSchedulerImpl to the ReminderScheduler interface.
     * Hilt will provide ReminderSchedulerImpl whenever ReminderScheduler is requested.
     */
    @Binds
    @Singleton // Match the scope of the component (SingletonComponent)
    abstract fun bindReminderScheduler(
        reminderSchedulerImpl: ReminderSchedulerImpl
    ): ReminderScheduler
} 