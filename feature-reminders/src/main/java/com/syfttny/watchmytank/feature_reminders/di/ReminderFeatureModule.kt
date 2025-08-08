package com.syfttny.watchmytank.feature_reminders.di


import com.syfttny.watchmytank.domain.use_case.ScheduleReminderUseCase
import com.syfttny.watchmytank.feature_reminders.use_case.ScheduleReminderUseCaseImpl


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) 
abstract class ReminderFeatureModule {

    
    @Binds
    @ViewModelScoped
    abstract fun bindScheduleReminderUseCase(impl: ScheduleReminderUseCaseImpl): ScheduleReminderUseCase

    
    
    
} 