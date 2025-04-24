package com.syfttny.watchmytank.domain.scheduler

import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Interface for scheduling and canceling reminder background work.
 */
interface ReminderScheduler {
    /**
     * Schedules the background worker for the given reminder.
     * Calculates the initial delay based on the reminder's nextTriggerTime.
     * If a worker already exists for this reminder, it might be cancelled and replaced.
     *
     * @param reminder The reminder to schedule work for.
     */
    suspend fun schedule(reminder: Reminder) // Made suspend as WorkManager operations can be async

    /**
     * Cancels any scheduled background work for the given reminder ID.
     *
     * @param reminderId The ID of the reminder whose work should be cancelled.
     */
    suspend fun cancel(reminderId: Long) // Made suspend as WorkManager operations can be async
} 