package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Use case to update an existing reminder.
 */
interface UpdateReminderUseCase {
    /**
     * @param reminder The reminder with updated information.
     *                 The ID field must match an existing reminder.
     */
    suspend operator fun invoke(reminder: Reminder)
} 