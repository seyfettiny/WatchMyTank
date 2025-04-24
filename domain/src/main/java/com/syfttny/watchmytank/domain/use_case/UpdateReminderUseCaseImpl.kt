package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import javax.inject.Inject

/**
 * Use case for updating an existing reminder.
 */
class UpdateReminderUseCaseImpl @Inject constructor(
    private val reminderRepository: ReminderRepository
) : UpdateReminderUseCase {

    /**
     * Updates the given reminder in the repository.
     * @param reminder The reminder object with updated details.
     */
    override suspend operator fun invoke(reminder: Reminder) {
        // Input validation could be added here if needed (e.g., check name length)
        reminderRepository.updateReminder(reminder)
    }
} 