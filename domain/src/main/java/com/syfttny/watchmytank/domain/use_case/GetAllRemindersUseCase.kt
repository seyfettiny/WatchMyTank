package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a stream of all reminders.
 */
interface GetAllRemindersUseCase {
    /**
     * Returns a Flow emitting the list of all reminders, ordered appropriately (e.g., by name or creation time).
     */
    operator fun invoke(): Flow<List<Reminder>>
} 