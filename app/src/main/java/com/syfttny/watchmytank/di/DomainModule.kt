package com.syfttny.watchmytank.di

import com.syfttny.watchmytank.domain.use_case.LogParameterUseCase
import com.syfttny.watchmytank.domain.use_case.LogParameterUseCaseImpl

// Reminder Use Cases
import com.syfttny.watchmytank.domain.use_case.AddReminderUseCase
import com.syfttny.watchmytank.domain.use_case.AddReminderUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.GetReminderUseCase
import com.syfttny.watchmytank.domain.use_case.GetReminderUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.UpdateReminderUseCase
import com.syfttny.watchmytank.domain.use_case.UpdateReminderUseCaseImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module responsible for providing implementations for domain layer interfaces (Use Cases).
 * This module resides in the :app layer to keep the :domain layer framework-agnostic.
 */
@Module
@InstallIn(SingletonComponent::class) // Or ViewModelComponent etc. depending on scope needed
abstract class DomainModule {

    // --- Parameter Use Cases ---

    @Binds
    @Singleton
    abstract fun bindLogParameterUseCase(
        impl: LogParameterUseCaseImpl
    ): LogParameterUseCase

    // --- Reminder Use Cases ---

    @Binds
    @Singleton
    abstract fun bindGetReminderUseCase(
        impl: GetReminderUseCaseImpl
    ): GetReminderUseCase

    @Binds
    @Singleton
    abstract fun bindAddReminderUseCase(
        impl: AddReminderUseCaseImpl
    ): AddReminderUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateReminderUseCase(
        impl: UpdateReminderUseCaseImpl
    ): UpdateReminderUseCase
} 