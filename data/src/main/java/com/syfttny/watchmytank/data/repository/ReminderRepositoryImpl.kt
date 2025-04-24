package com.syfttny.watchmytank.data.repository

import com.syfttny.watchmytank.data.local.dao.ReminderDao
import com.syfttny.watchmytank.data.mapper.toDomain
import com.syfttny.watchmytank.data.mapper.toEntity
import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [ReminderRepository] that uses Room for local persistence.
 */
@Singleton // Provided as a singleton by Hilt
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override suspend fun insertReminder(reminder: Reminder): Long {
        val entity = reminder.toEntity()
        // Use insert which handles both insert and update (replace strategy)
        return reminderDao.insertReminder(entity)
    }

    override suspend fun deleteReminder(id: Long) {
        reminderDao.deleteReminderById(id)
    }

    override suspend fun getReminderById(id: Long): Reminder? {
        val entity = reminderDao.getReminderById(id)
        return entity?.toDomain()
    }

    override fun getReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders().map { entities ->
            entities.toDomain()
        }
    }

    override fun getAllRemindersStream(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders().map { entities ->
            entities.toDomain()
        }
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toEntity())
    }
} 