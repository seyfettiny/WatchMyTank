package com.syfttny.watchmytank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.syfttny.watchmytank.data.local.DatabaseConverters
import com.syfttny.watchmytank.domain.model.ReminderType
import java.time.LocalDateTime

@Entity(tableName = "reminders")
@TypeConverters(DatabaseConverters::class)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: ReminderType, 
    val frequencyDays: Int? = null,
    val cronExpression: String? = null,
    val creationTime: LocalDateTime, 
    val nextTriggerTime: LocalDateTime, 
    val isEnabled: Boolean = true,
    val lastTriggeredTime: LocalDateTime? = null 
) 