package com.syfttny.watchmytank.domain.scheduler

import com.syfttny.watchmytank.domain.model.Reminder

interface ReminderScheduler {
    suspend fun schedule(reminder: Reminder) 
    suspend fun cancel(reminderId: Long) 
} 