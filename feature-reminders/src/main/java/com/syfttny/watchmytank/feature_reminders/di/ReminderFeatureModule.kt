package com.syfttny.watchmytank.feature_reminders.di

import com.syfttny.watchmytank.domain.use_case.ScheduleReminderUseCase
import com.syfttny.watchmytank.feature_reminders.use_case.ScheduleReminderUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Hilt module providing dependencies specific to the reminder feature,
 * like the implementation for ScheduleReminderUseCase which lives in this module.
 */
@Module
@InstallIn(ViewModelComponent::class) // Scope to ViewModels using this UseCase
abstract class ReminderFeatureModule {

    @Binds
    @ViewModelScoped // Use the same scope as the InstallIn component
    abstract fun bindScheduleReminderUseCase(impl: ScheduleReminderUseCaseImpl): ScheduleReminderUseCase

    // Add bindings for other feature-specific use case implementations here if needed
} 