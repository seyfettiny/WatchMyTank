package com.syfttny.watchmytank.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.syfttny.watchmytank.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Reminder entity.
 */
@Dao
interface ReminderDao {

    /**
     * Inserts a new reminder into the database.
     * If a reminder with the same ID already exists, it replaces it.
     * @return The row ID of the newly inserted reminder.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    /**
     * Updates an existing reminder in the database.
     */
    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    /**
     * Deletes a reminder from the database.
     */
    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    /**
     * Deletes a reminder by its ID.
     * @param id The ID of the reminder to delete.
     */
    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    /**
     * Retrieves a specific reminder by its ID.
     * @param id The ID of the reminder to retrieve.
     * @return A Flow emitting the ReminderEntity, or null if not found.
     */
    @Query("SELECT * FROM reminders WHERE id = :id")
    fun getReminderById(id: Long): Flow<ReminderEntity?>

    /**
     * Retrieves all reminders from the database, ordered by creation time (newest first).
     * @return A Flow emitting the list of all ReminderEntity objects.
     */
    @Query("SELECT * FROM reminders ORDER BY creationTime DESC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    /**
     * Retrieves all *enabled* reminders, ordered by their next trigger time (soonest first).
     * Useful for scheduling the next upcoming reminder.
     * @return A Flow emitting the list of enabled ReminderEntity objects.
     */
    @Query("SELECT * FROM reminders WHERE isEnabled = 1 ORDER BY nextTriggerTime ASC")
    fun getAllEnabledRemindersOrderedByNextTrigger(): Flow<List<ReminderEntity>>

     /**
     * Retrieves all reminders that are due to trigger before or at a specific time.
     * Useful for finding reminders whose notifications should be fired.
     * @param currentTime The timestamp to compare against (as epoch seconds).
     * @return A list of due ReminderEntity objects.
     */
    @Query("SELECT * FROM reminders WHERE isEnabled = 1 AND nextTriggerTime <= :currentTime")
    suspend fun getDueReminders(currentTime: Long): List<ReminderEntity>
} 