package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.repository.ReminderRepository
import javax.inject.Inject

/**
 * Implementation for [DeleteReminderUseCase].
 */
class DeleteReminderUseCaseImpl @Inject constructor(
    private val reminderRepository: ReminderRepository
) : DeleteReminderUseCase {

    override suspend operator fun invoke(reminderId: Long) {
        reminderRepository.deleteReminder(reminderId)
    }
} 