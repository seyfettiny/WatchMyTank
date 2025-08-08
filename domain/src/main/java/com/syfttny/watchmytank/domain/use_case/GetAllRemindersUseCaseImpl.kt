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
        
        
        
        return reminderRepository.getAllRemindersStream()
    }
} 