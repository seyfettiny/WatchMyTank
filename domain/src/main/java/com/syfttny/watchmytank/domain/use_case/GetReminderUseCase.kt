package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.Reminder

/**
 * Use case to retrieve a single reminder by its ID.
 */
interface GetReminderUseCase {
    suspend operator fun invoke(id: Long): Reminder?
} 