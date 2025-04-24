package com.syfttny.watchmytank.feature_reminders.scheduler

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.model.ReminderType
import com.syfttny.watchmytank.domain.scheduler.ReminderScheduler
import com.syfttny.watchmytank.feature_reminders.worker.ReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun schedule(reminder: Reminder) {
        // Only schedule enabled reminders that have a valid ID
        if (!reminder.isEnabled || reminder.id <= 0) {
            Log.w("ReminderScheduler", "Skipping schedule for disabled or invalid ID reminder: ${reminder.id}")
            // Optionally cancel existing work if it's being disabled
            if (!reminder.isEnabled && reminder.id > 0) {
                cancel(reminder.id)
            }
            return
        }

        val initialNextTrigger = calculateInitialTriggerDateTime(reminder)

        if (initialNextTrigger == null) {
            Log.e("ReminderScheduler", "Could not calculate initial trigger time for reminder ${reminder.id}. Aborting schedule.")
            // Consider cancelling existing work if calculation fails?
            cancel(reminder.id)
            return
        }

        val now = LocalDateTime.now()
        val initialDelay = Duration.between(now, initialNextTrigger).coerceAtLeast(Duration.ZERO) // Ensure non-negative delay

        Log.d("ReminderScheduler", "Calculated initial trigger for ${reminder.id}: $initialNextTrigger (Delay: $initialDelay)")


        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(initialDelay)
            .setInputData(workDataOf(ReminderWorker.KEY_REMINDER_ID to reminder.id))
            .addTag(getWorkTag(reminder.id))
            .build()

        // Use enqueueUniqueWork with REPLACE policy. This ensures that if the reminder is
        // saved again (edited), the old worker instance is cancelled and replaced with
        // the new one based on the updated schedule.
        workManager.enqueueUniqueWork(
            getWorkName(reminder.id),
            ExistingWorkPolicy.REPLACE, // Replace existing work if reminder is updated
            workRequest
        )

        Log.i("ReminderScheduler", "Enqueued unique work for reminder ${reminder.id} with tag ${getWorkTag(reminder.id)}")
    }

    // Helper function to calculate the very first trigger time
    private fun calculateInitialTriggerDateTime(reminder: Reminder): LocalDateTime? {
        val now = LocalDateTime.now()
        val targetHour = reminder.triggerHour ?: 9 // Default to 9 AM if not set
        val targetMinute = reminder.triggerMinute ?: 0 // Default to 0 minutes if not set
        val targetTime = LocalTime.of(targetHour, targetMinute)

        return when (reminder.type) {
            ReminderType.DAILY -> {
                val todayTrigger = now.toLocalDate().atTime(targetTime)
                if (todayTrigger.isAfter(now)) {
                    todayTrigger // If target time is later today
                } else {
                    todayTrigger.plusDays(1) // Target time has passed, schedule for tomorrow
                }
            }
            ReminderType.EVERY_N_DAYS -> {
                val days = reminder.frequencyDays ?: 1 // Default to 1 if null? Or handle error?
                if (days <= 0) return null // Invalid frequency

                // Start calculation from today at the target time
                var potentialTrigger = now.toLocalDate().atTime(targetTime)
                if (potentialTrigger.isBefore(now)) {
                    // If target time already passed today, start calculation basis from tomorrow
                    potentialTrigger = potentialTrigger.plusDays(1)
                }
                // In a real scenario, we might need to align this with 'creationTime'
                // or 'lastTriggeredTime' to respect the N-day cycle accurately.
                // For initial scheduling simplicity, let's assume the cycle *can* start today/tomorrow.
                // This might mean the *first* interval is shorter than N days if created mid-cycle.
                // The ReminderWorker's calculateNextTriggerTime should handle subsequent intervals correctly.
                potentialTrigger // First trigger is today/tomorrow at target time.
            }
            ReminderType.CRON -> {
                Log.w("ReminderScheduler", "CRON scheduling not yet implemented.")
                // TODO: Implement CRON parsing and next execution calculation
                null // Return null until implemented
            }
        }
    }

    override suspend fun cancel(reminderId: Long) {
        if (reminderId <= 0) return // Ignore invalid IDs
        val workTag = getWorkTag(reminderId)
        val workName = getWorkName(reminderId) // Also cancel by unique name
        workManager.cancelUniqueWork(workName) // Cancel by unique name first
        workManager.cancelAllWorkByTag(workTag) // Then cancel by tag (might be redundant but safe)
        Log.i("ReminderScheduler", "Attempted cancellation for reminder $reminderId (Name: $workName, Tag: $workTag)")
    }

    // --- Helper functions for WorkManager tags/names ---
    private fun getWorkTag(reminderId: Long) = "reminder_work_tag_$reminderId"
    private fun getWorkName(reminderId: Long) = "reminder_work_name_$reminderId" // Unique name for enqueueUniqueWork

} 