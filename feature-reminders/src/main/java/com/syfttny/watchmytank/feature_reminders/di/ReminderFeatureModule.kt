package com.syfttny.watchmytank.feature_reminders.di

// Only import interfaces/implementations needed for bindings WITHIN this feature module
import com.syfttny.watchmytank.domain.use_case.ScheduleReminderUseCase
import com.syfttny.watchmytank.feature_reminders.use_case.ScheduleReminderUseCaseImpl
// Domain use case implementations are no longer needed here

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Hilt module providing dependencies specific to the reminder feature.
 * Domain-layer use cases are provided by the DomainModule in the :app layer.
 */
@Module
@InstallIn(ViewModelComponent::class) // Scope to ViewModels using these UseCases
abstract class ReminderFeatureModule {

    // Bind feature-specific implementations to domain interfaces if needed
    @Binds
    @ViewModelScoped
    abstract fun bindScheduleReminderUseCase(impl: ScheduleReminderUseCaseImpl): ScheduleReminderUseCase

    // Bindings for GetReminderUseCase, AddReminderUseCase, UpdateReminderUseCase
    // have been moved to DomainModule in the :app layer to adhere to
    // dependency rules and centralize domain DI setup.
} 