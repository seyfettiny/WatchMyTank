package com.syfttny.watchmytank.data.local

import androidx.room.TypeConverter
import com.syfttny.watchmytank.domain.model.ParameterType
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

    // ParameterType Converters
    @TypeConverter
    fun fromParameterType(value: String?): ParameterType? {
        return value?.let { enumName ->
            try {
                ParameterType.valueOf(enumName)
            } catch (e: IllegalArgumentException) {
                null // Handle cases where the stored enum name might be invalid
            }
        }
    }

    @TypeConverter
    fun parameterToString(type: ParameterType?): String? {
        return type?.name
    }
} 