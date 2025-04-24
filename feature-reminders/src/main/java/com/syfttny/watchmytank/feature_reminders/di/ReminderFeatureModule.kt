package com.syfttny.watchmytank.feature_reminders.di

import com.syfttny.watchmytank.domain.use_case.*
import com.syfttny.watchmytank.feature_reminders.use_case.ScheduleReminderUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Hilt module providing dependencies specific to the reminder feature,
 * including Use Case implementations.
 */
@Module
@InstallIn(ViewModelComponent::class) // Scope to ViewModels using these UseCases
abstract class ReminderFeatureModule {

    @Binds
    @ViewModelScoped
    abstract fun bindScheduleReminderUseCase(impl: ScheduleReminderUseCaseImpl): ScheduleReminderUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetReminderUseCase(impl: GetReminderUseCaseImpl): GetReminderUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindAddReminderUseCase(impl: AddReminderUseCaseImpl): AddReminderUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateReminderUseCase(impl: UpdateReminderUseCaseImpl): UpdateReminderUseCase

    // TODO: We will likely need bindings for DeleteReminderUseCase later
} 