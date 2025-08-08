package com.syfttny.watchmytank.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.local.dao.ReminderDao
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.data.local.entity.ReminderEntity

/**
 * The main Room database for the application.
 * Includes entities for Reminders and Water Parameters.
 * Utilizes custom type converters defined in [DatabaseConverters].
 */
@Database(
    entities = [
        ReminderEntity::class,
        ParameterLogEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao
    abstract fun parameterDao(): ParameterDao
} 