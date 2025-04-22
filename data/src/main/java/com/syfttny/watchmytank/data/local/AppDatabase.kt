package com.syfttny.watchmytank.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.syfttny.watchmytank.data.local.dao.ReminderDao
import com.syfttny.watchmytank.data.local.entity.ReminderEntity

/**
 * The main Room database for the application.
 * It includes the Reminder entity and utilizes custom type converters.
 */
@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = true // Set to true to export schema for migrations, false if not needed yet.
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the Data Access Object for Reminders.
     */
    abstract fun reminderDao(): ReminderDao

    // Add other DAOs here as the app grows
    // abstract fun waterParameterDao(): WaterParameterDao
} 