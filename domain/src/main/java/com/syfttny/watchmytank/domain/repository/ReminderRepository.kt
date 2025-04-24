package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for accessing and modifying Reminder data.
 * Implementations will reside in the data layer.
 */
interface ReminderRepository {

    /**
     * Retrieves a specific reminder by its unique ID.
     * @param id The ID of the reminder to retrieve.
     * @return The [Reminder] object if found, null otherwise.
     */
    suspend fun getReminderById(id: Long): Reminder?

    /**
     * Retrieves all reminders as a Flow, allowing observation of changes.
     * This might be deprecated or removed in favor of getAllRemindersStream if they serve the same purpose.
     * @return A [Flow] emitting a list of all [Reminder] objects.
     */
    fun getReminders(): Flow<List<Reminder>>

    /**
     * Retrieves all reminders as a Flow, emitting updates whenever the underlying data changes.
     * @return A [Flow] emitting a list of all [Reminder] objects.
     */
    fun getAllRemindersStream(): Flow<List<Reminder>>

    /**
     * Inserts a new reminder.
     * @param reminder The [Reminder] object to insert.
     * @return The ID of the newly inserted reminder.
     */
    suspend fun insertReminder(reminder: Reminder): Long

    /**
     * Updates an existing reminder.
     * @param reminder The [Reminder] object with updated details.
     */
    suspend fun updateReminder(reminder: Reminder)

    /**
     * Deletes a reminder by its unique ID.
     * @param id The ID of the reminder to delete.
     */
    suspend fun deleteReminder(id: Long)
} 