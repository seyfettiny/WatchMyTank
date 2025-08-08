package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import javax.inject.Inject

class UpdateReminderUseCaseImpl @Inject constructor(
    private val reminderRepository: ReminderRepository
) : UpdateReminderUseCase {
    override suspend operator fun invoke(reminder: Reminder) {
        
        reminderRepository.updateReminder(reminder)
    }
} 