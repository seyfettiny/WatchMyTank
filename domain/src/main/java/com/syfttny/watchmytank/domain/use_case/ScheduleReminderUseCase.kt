package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Use case responsible for scheduling reminder work using WorkManager.
 */
interface ScheduleReminderUseCase {

    /**
     * Schedules a work request for the given reminder.
     * This will typically enqueue a OneTimeWorkRequest with the necessary delay
     * and input data (reminder ID).
     *
     * @param reminder The reminder to schedule.
     */
    suspend operator fun invoke(reminder: Reminder)

    /**
     * Cancels any scheduled work associated with the given reminder ID.
     *
     * @param reminderId The ID of the reminder whose work should be cancelled.
     */
    suspend fun cancel(reminderId: Long)
} 