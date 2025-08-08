package com.syfttny.watchmytank.data.di 



import com.syfttny.watchmytank.domain.use_case.DeleteReminderUseCase
import com.syfttny.watchmytank.domain.use_case.DeleteReminderUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.GetAllRemindersUseCase
import com.syfttny.watchmytank.domain.use_case.GetAllRemindersUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.GetUnsyncedLogCountUseCase
import com.syfttny.watchmytank.domain.use_case.GetUnsyncedLogCountUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.LogParameterUseCase
import com.syfttny.watchmytank.domain.use_case.LogParameterUseCaseImpl


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetAllRemindersUseCase(impl: GetAllRemindersUseCaseImpl): GetAllRemindersUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteReminderUseCase(impl: DeleteReminderUseCaseImpl): DeleteReminderUseCase

    @Binds
    @Singleton
    abstract fun bindGetUnsyncedLogCountUseCase(impl: GetUnsyncedLogCountUseCaseImpl): GetUnsyncedLogCountUseCase
} 