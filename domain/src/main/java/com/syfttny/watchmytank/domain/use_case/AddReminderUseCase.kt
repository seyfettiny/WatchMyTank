package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Use case to add a new reminder.
 */
interface AddReminderUseCase {
    /**
     * @param reminder The reminder to add.
     * @return The ID of the newly added reminder.
     */
    suspend operator fun invoke(reminder: Reminder): Long
} 