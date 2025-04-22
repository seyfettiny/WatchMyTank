package com.syfttny.watchmytank.data.local

import androidx.room.TypeConverter
import com.syfttny.watchmytank.domain.model.ReminderType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Room TypeConverters for custom data types used in the database entities.
 */
class DatabaseConverters {

    // LocalDateTime Converters
    // Room doesn't natively support LocalDateTime, so we convert to/from Long (epoch seconds)

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }

    // ReminderType Converters
    // Convert the enum to/from its String representation (safer than using ordinal)

    @TypeConverter
    fun fromReminderType(value: String?): ReminderType? {
        return value?.let { ReminderType.valueOf(it) }
    }

    @TypeConverter
    fun reminderTypeToString(type: ReminderType?): String? {
        return type?.name
    }
} 