package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for accessing reminder data.
 * This abstracts the data source (local or remote) from the domain layer.
 */
interface ReminderRepository {

    /**
     * Adds a new reminder or updates an existing one.
     * @param reminder The reminder to add or update.
     * @return The ID of the inserted/updated reminder.
     */
    suspend fun saveReminder(reminder: Reminder): Long

    /**
     * Updates an existing reminder.
     * @param reminder The reminder with updated values.
     */
    suspend fun updateReminder(reminder: Reminder)

    /**
     * Deletes a specific reminder.
     * @param reminder The reminder to delete.
     */
    suspend fun deleteReminder(reminder: Reminder)

    /**
     * Deletes a reminder by its unique ID.
     * @param id The ID of the reminder to delete.
     */
    suspend fun deleteReminderById(id: Long)

    /**
     * Retrieves a specific reminder by its ID.
     * @param id The ID of the reminder.
     * @return A Flow emitting the Reminder, or null if not found.
     */
    fun getReminderById(id: Long): Flow<Reminder?>

    /**
     * Retrieves a stream of all reminders, typically ordered for display.
     * @return A Flow emitting the list of all Reminders.
     */
    fun getAllRemindersStream(): Flow<List<Reminder>>

    /**
     * Retrieves a stream of all *enabled* reminders, ordered by the next trigger time.
     * @return A Flow emitting the list of enabled Reminders.
     */
    fun getEnabledRemindersStreamOrderedByNextTrigger(): Flow<List<Reminder>>

    /**
     * Retrieves reminders that are due based on the current time.
     * @param currentTimeEpochSeconds The current time as epoch seconds UTC.
     * @return A list of due Reminders.
     */
    suspend fun getDueReminders(currentTimeEpochSeconds: Long): List<Reminder>
} 