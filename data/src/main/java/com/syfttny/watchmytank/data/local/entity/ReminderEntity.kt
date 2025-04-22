package com.syfttny.watchmytank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.syfttny.watchmytank.data.local.DatabaseConverters
import com.syfttny.watchmytank.domain.model.ReminderType
import java.time.LocalDateTime

/**
 * Represents a reminder stored in the local Room database.
 */
@Entity(tableName = "reminders")
@TypeConverters(DatabaseConverters::class)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: ReminderType, // Stored as String via TypeConverter
    val frequencyDays: Int? = null,
    val cronExpression: String? = null,
    val creationTime: LocalDateTime, // Stored as Long via TypeConverter
    val nextTriggerTime: LocalDateTime, // Stored as Long via TypeConverter
    val isEnabled: Boolean = true,
    val lastTriggeredTime: LocalDateTime? = null // Stored as Long via TypeConverter
) 