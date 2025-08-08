package com.syfttny.watchmytank.feature_reminders.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.syfttny.watchmytank.domain.repository.ReminderRepository
import com.syfttny.watchmytank.feature_reminders.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderRepository: ReminderRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_REMINDER_ID = "REMINDER_ID"
        private const val TAG = "ReminderWorker"
    }

    override suspend fun doWork(): Result {
        val reminderId = inputData.getLong(KEY_REMINDER_ID, -1L)
        if (reminderId == -1L) {
            Log.e(TAG, "Invalid Reminder ID received.")
            return Result.failure()
        }

        Log.d(TAG, "Worker started for Reminder ID: $reminderId")

        // Permission check is important before IO context
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "POST_NOTIFICATIONS permission not granted. Cannot process reminder $reminderId.")
            
            return Result.failure()
        }

        return withContext(Dispatchers.IO) {
            try {
                val reminder = reminderRepository.getReminderById(reminderId)

                if (reminder == null || !reminder.isEnabled) {
                    Log.w(TAG, "Reminder not found or disabled: $reminderId. Stopping chain.")
                    
                    return@withContext Result.success() 
                }

                
                Log.i(TAG, "Showing notification for reminder: ${reminder.name} ($reminderId)")
                notificationHelper.showReminderNotification(reminder)
                val currentTime = LocalDateTime.now() 

                
                val nextTriggerTime = reminder.calculateNextTriggerTime()
                if (nextTriggerTime == null) {
                    Log.e(TAG, "Could not calculate next trigger time for reminder ${reminder.id} (type: ${reminder.type}). Stopping chain.")
                    
                    reminder.lastTriggeredTime = currentTime
                    reminderRepository.updateReminder(reminder) 
                    return@withContext Result.success() 
                }

                
                reminder.lastTriggeredTime = currentTime
                reminder.nextTriggerTime = nextTriggerTime
                reminderRepository.updateReminder(reminder)
                Log.i(TAG, "Updated reminder ${reminder.id}. Next trigger: $nextTriggerTime")

                
                scheduleNextWork(reminder.id, nextTriggerTime)

                Log.i(TAG, "Successfully processed and rescheduled reminder ${reminder.id}")
                Result.success()
            } catch (e: Exception) {
                Log.e(TAG, "Error processing reminder $reminderId", e)
                Result.retry() 
            }
        }
    }

    private fun scheduleNextWork(reminderId: Long, nextTriggerTime: LocalDateTime) {
        val now = LocalDateTime.now()
        val delay = Duration.between(now, nextTriggerTime)

        
        if (delay.isNegative || delay.isZero) {
            Log.w(TAG, "Calculated next trigger time $nextTriggerTime is in the past for reminder $reminderId. Scheduling immediately, but check logic.")
            
        }

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay)
            .setInputData(workDataOf(KEY_REMINDER_ID to reminderId))
            
            .addTag("reminder_work_$reminderId") 
            .build()

        WorkManager.getInstance(appContext).enqueue(workRequest)
        Log.d(TAG, "Enqueued next worker for reminder $reminderId with delay: $delay")
    }
} 