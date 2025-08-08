package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import java.time.LocalDateTime
import javax.inject.Inject

class AddReminderUseCaseImpl @Inject constructor(
    private val repository: ReminderRepository
) : AddReminderUseCase {
    override suspend operator fun invoke(reminder: Reminder): Long {
        
        
        val reminderToInsert = reminder.copy(
            id = 0, 
            creationTime = reminder.creationTime ?: LocalDateTime.now(),
            
            
            nextTriggerTime = reminder.calculateNextTriggerTime(LocalDateTime.now()) ?: LocalDateTime.now().plusDays(1) 
        )
        
        if (reminderToInsert.name.isBlank()) {
             throw IllegalArgumentException("Reminder name cannot be blank.")
        }
        return repository.insertReminder(reminderToInsert)
    }
} 