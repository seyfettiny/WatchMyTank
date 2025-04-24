package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import javax.inject.Inject

/**
 * Implementation for [GetReminderUseCase].
 */
class GetReminderUseCaseImpl @Inject constructor(
    private val repository: ReminderRepository
) : GetReminderUseCase {
    override suspend operator fun invoke(id: Long): Reminder? {
        // Basic validation or logic could be added here if needed
        if (id <= 0) return null
        return repository.getReminderById(id)
    }
} 