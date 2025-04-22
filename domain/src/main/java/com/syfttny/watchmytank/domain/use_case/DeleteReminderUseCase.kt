package com.syfttny.watchmytank.domain.use_case

/**
 * Use case to delete a specific reminder.
 */
interface DeleteReminderUseCase {
    /**
     * Deletes the reminder with the given ID.
     * @param reminderId The ID of the reminder to delete.
     */
    suspend operator fun invoke(reminderId: Long)
} 