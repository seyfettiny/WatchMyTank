package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Implementation for [AddReminderUseCase].
 */
class AddReminderUseCaseImpl @Inject constructor(
    private val repository: ReminderRepository
) : AddReminderUseCase {
    override suspend operator fun invoke(reminder: Reminder): Long {
        // Add validation or default value logic here if needed
        // Example: Ensure creation time is set, calculate initial next trigger time
        val reminderToInsert = reminder.copy(
            id = 0, // Ensure ID is 0 for insertion
            creationTime = reminder.creationTime ?: LocalDateTime.now(),
            // Calculate initial next trigger time based on type/frequency
            // This logic could be more complex and potentially live in its own use case
            nextTriggerTime = reminder.calculateNextTriggerTime(LocalDateTime.now()) ?: LocalDateTime.now().plusDays(1) // Fallback
        )
        // More complex validation (e.g., CRON string format) could happen here
        if (reminderToInsert.name.isBlank()) {
             throw IllegalArgumentException("Reminder name cannot be blank.")
        }
        return repository.insertReminder(reminderToInsert)
    }
} 