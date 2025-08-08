package com.syfttny.watchmytank.feature_reminders.di

import com.syfttny.watchmytank.domain.scheduler.ReminderScheduler
import com.syfttny.watchmytank.feature_reminders.scheduler.ReminderSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) 
abstract class SchedulerModule {
    @Binds
    @Singleton 
    abstract fun bindReminderScheduler(
        reminderSchedulerImpl: ReminderSchedulerImpl
    ): ReminderScheduler
} 