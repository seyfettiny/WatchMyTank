package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation for [GetAllRemindersUseCase].
 */
class GetAllRemindersUseCaseImpl @Inject constructor(
    private val reminderRepository: ReminderRepository
) : GetAllRemindersUseCase {

    override operator fun invoke(): Flow<List<Reminder>> {
        // Directly return the stream from the repository
        // Sorting/filtering could be added here if needed,
        // but the repository already provides a sorted stream by default.
        return reminderRepository.getAllRemindersStream()
    }
} 