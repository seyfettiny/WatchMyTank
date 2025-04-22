package com.syfttny.watchmytank.data.mapper

import com.syfttny.watchmytank.data.local.entity.ReminderEntity
import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Maps a [ReminderEntity] (data layer) to a [Reminder] (domain layer).
 */
fun ReminderEntity.toDomain(): Reminder {
    return Reminder(
        id = this.id,
        name = this.name,
        type = this.type,
        frequencyDays = this.frequencyDays,
        cronExpression = this.cronExpression,
        creationTime = this.creationTime,
        nextTriggerTime = this.nextTriggerTime,
        isEnabled = this.isEnabled,
        lastTriggeredTime = this.lastTriggeredTime
    )
}

/**
 * Maps a [Reminder] (domain layer) to a [ReminderEntity] (data layer).
 */
fun Reminder.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        frequencyDays = this.frequencyDays,
        cronExpression = this.cronExpression,
        creationTime = this.creationTime,
        nextTriggerTime = this.nextTriggerTime,
        isEnabled = this.isEnabled,
        lastTriggeredTime = this.lastTriggeredTime
    )
}

/**
 * Maps a list of [ReminderEntity] objects to a list of [Reminder] objects.
 */
fun List<ReminderEntity>.toDomain(): List<Reminder> {
    return this.map { it.toDomain() }
} 