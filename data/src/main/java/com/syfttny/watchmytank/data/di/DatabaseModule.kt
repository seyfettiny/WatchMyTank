package com.syfttny.watchmytank.data.di

import android.content.Context
import androidx.room.Room
import com.syfttny.watchmytank.data.local.AppDatabase
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.local.dao.ReminderDao
import com.syfttny.watchmytank.data.repository.ParameterRepositoryImpl
import com.syfttny.watchmytank.data.repository.ReminderRepositoryImpl
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "watchmytank_database"
        )
        // Add migrations here if needed in the future
        // .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration() // Use only during development!
        .build()
    }

    @Provides
    fun provideReminderDao(appDatabase: AppDatabase): ReminderDao {
        return appDatabase.reminderDao()
    }

    @Provides
    fun provideParameterDao(appDatabase: AppDatabase): ParameterDao {
        return appDatabase.parameterDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository

    @Binds
    @Singleton
    abstract fun bindParameterRepository(impl: ParameterRepositoryImpl): ParameterRepository

    // Bind other repositories here in the future
} 