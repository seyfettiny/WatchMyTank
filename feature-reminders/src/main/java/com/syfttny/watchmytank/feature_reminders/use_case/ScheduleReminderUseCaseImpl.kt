package com.syfttny.watchmytank.feature_reminders.use_case

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.syfttny.watchmytank.feature_reminders.worker.ReminderWorker
import com.syfttny.watchmytank.domain.model.Reminder
import com.syfttny.watchmytank.domain.use_case.ScheduleReminderUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleReminderUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ScheduleReminderUseCase {

    private val workManager = WorkManager.getInstance(context)

    override suspend operator fun invoke(reminder: Reminder) {
        if (!reminder.isEnabled) {
            // If the reminder is disabled, cancel any existing work for it
            cancel(reminder.id)
            return
        }

        val now = LocalDateTime.now(ZoneOffset.UTC)
        // Make sure reminder.nextTriggerTime is not null
        val nextTrigger = reminder.nextTriggerTime ?: run {
            // Handle null nextTriggerTime: maybe log error, or don't schedule
            // For now, let's log and return, as scheduling is impossible.
            println("Error: Cannot schedule reminder ${reminder.id} with null nextTriggerTime")
            cancel(reminder.id) // Cancel any potentially existing work
            return
        }
        val initialDelay = Duration.between(now, nextTrigger)

        // Ensure delay is not negative (i.e., reminder time is in the future)
        if (initialDelay.isNegative || initialDelay.isZero) {
            // Handle case where next trigger time is in the past or now.
            // Options: Trigger immediately, log error, or calculate next valid time?
            // For now, let's schedule it with a minimal delay (e.g., 1 second) to trigger ASAP.
            // A more robust solution would calculate the *next* valid trigger time based on frequency.
            // TODO: Implement proper calculation for past-due trigger times
            println("Warning: Scheduling reminder ${reminder.id} for immediate trigger (nextTriggerTime was in the past)")
            scheduleWork(reminder.id, Duration.ofSeconds(1))
        } else {
            scheduleWork(reminder.id, initialDelay)
        }
    }

    override suspend fun cancel(reminderId: Long) {
        val workTag = createWorkTag(reminderId)
        workManager.cancelUniqueWork(workTag)
        println("Cancelled work for reminder ID: $reminderId (tag: $workTag)")
    }

    private fun scheduleWork(reminderId: Long, initialDelay: Duration) {
        val inputData = Data.Builder()
            .putLong(ReminderWorker.KEY_REMINDER_ID, reminderId)
            .build()

        // Define constraints if necessary (e.g., network, battery)
        val constraints = Constraints.Builder()
            // .setRequiredNetworkType(NetworkType.CONNECTED) // Example
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(initialDelay)
            .setInputData(inputData)
            .setConstraints(constraints)
            .addTag(createWorkTag(reminderId)) // Use tag for cancellation
            .build()

        // Use unique work to prevent duplicates if scheduled multiple times
        // REPLACE existing work if a reminder is updated with a new time
        workManager.enqueueUniqueWork(
            createWorkTag(reminderId), // Unique work name based on ID
            ExistingWorkPolicy.REPLACE, // Replace existing work for this reminder
            workRequest
        )
        println("Scheduled work for reminder ID: $reminderId with delay: $initialDelay (tag: ${createWorkTag(reminderId)})")
    }

    /**
     * Creates a unique tag for WorkManager requests based on the reminder ID.
     */
    private fun createWorkTag(reminderId: Long): String {
        return "reminder_work_${reminderId}"
    }
} 