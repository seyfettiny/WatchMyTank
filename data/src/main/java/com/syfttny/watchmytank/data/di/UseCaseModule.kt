package com.syfttny.watchmytank.data.di // Reverted package

// Remove import for ScheduleReminderUseCaseImpl as it's no longer bound here
// import com.syfttny.watchmytank.data.use_case.ScheduleReminderUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.DeleteReminderUseCase
import com.syfttny.watchmytank.domain.use_case.DeleteReminderUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.GetAllRemindersUseCase
import com.syfttny.watchmytank.domain.use_case.GetAllRemindersUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.GetUnsyncedLogCountUseCase
import com.syfttny.watchmytank.domain.use_case.GetUnsyncedLogCountUseCaseImpl
import com.syfttny.watchmytank.domain.use_case.LogParameterUseCase
import com.syfttny.watchmytank.domain.use_case.LogParameterUseCaseImpl
// Remove import for ScheduleReminderUseCase interface as it's no longer bound here
// import com.syfttny.watchmytank.domain.use_case.ScheduleReminderUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    // Removed bindScheduleReminderUseCase binding
    /*
    @Binds
    @Singleton
    abstract fun bindScheduleReminderUseCase(impl: ScheduleReminderUseCaseImpl): ScheduleReminderUseCase
    */

    @Binds
    @Singleton
    abstract fun bindGetAllRemindersUseCase(impl: GetAllRemindersUseCaseImpl): GetAllRemindersUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteReminderUseCase(impl: DeleteReminderUseCaseImpl): DeleteReminderUseCase

    // Removed binding for LogParameterUseCase
    /*
    @Binds
    @Singleton
    abstract fun bindLogParameterUseCase(impl: LogParameterUseCaseImpl): LogParameterUseCase
    */

    @Binds
    @Singleton
    abstract fun bindGetUnsyncedLogCountUseCase(impl: GetUnsyncedLogCountUseCaseImpl): GetUnsyncedLogCountUseCase

    // Bind other data/domain layer use case implementations here
} 