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
        
        if (!reminder.isEnabled || reminder.id <= 0) {
            Log.w("ReminderScheduler", "Skipping schedule for disabled or invalid ID reminder: ${reminder.id}")
            
            if (!reminder.isEnabled && reminder.id > 0) {
                cancel(reminder.id)
            }
            return
        }

        val initialNextTrigger = calculateInitialTriggerDateTime(reminder)

        if (initialNextTrigger == null) {
            Log.e("ReminderScheduler", "Could not calculate initial trigger time for reminder ${reminder.id}. Aborting schedule.")
            
            cancel(reminder.id)
            return
        }

        val now = LocalDateTime.now()
        val initialDelay = Duration.between(now, initialNextTrigger).coerceAtLeast(Duration.ZERO) 

        Log.d("ReminderScheduler", "Calculated initial trigger for ${reminder.id}: $initialNextTrigger (Delay: $initialDelay)")


        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(initialDelay)
            .setInputData(workDataOf(ReminderWorker.KEY_REMINDER_ID to reminder.id))
            .addTag(getWorkTag(reminder.id))
            .build()

        
        
        
        workManager.enqueueUniqueWork(
            getWorkName(reminder.id),
            ExistingWorkPolicy.REPLACE, 
            workRequest
        )

        Log.i("ReminderScheduler", "Enqueued unique work for reminder ${reminder.id} with tag ${getWorkTag(reminder.id)}")
    }

    
    private fun calculateInitialTriggerDateTime(reminder: Reminder): LocalDateTime? {
        val now = LocalDateTime.now()
        val targetHour = reminder.triggerHour ?: 9 
        val targetMinute = reminder.triggerMinute ?: 0 
        val targetTime = LocalTime.of(targetHour, targetMinute)

        return when (reminder.type) {
            ReminderType.DAILY -> {
                val todayTrigger = now.toLocalDate().atTime(targetTime)
                if (todayTrigger.isAfter(now)) {
                    todayTrigger 
                } else {
                    todayTrigger.plusDays(1) 
                }
            }
            ReminderType.EVERY_N_DAYS -> {
                val days = reminder.frequencyDays ?: 1 
                if (days <= 0) return null 

                
                var potentialTrigger = now.toLocalDate().atTime(targetTime)
                if (potentialTrigger.isBefore(now)) {
                    
                    potentialTrigger = potentialTrigger.plusDays(1)
                }
                
                
                
                
                
                potentialTrigger 
            }
            ReminderType.CRON -> {
                Log.w("ReminderScheduler", "CRON scheduling not yet implemented.")
                
                null 
            }
        }
    }

    override suspend fun cancel(reminderId: Long) {
        if (reminderId <= 0) return 
        val workTag = getWorkTag(reminderId)
        val workName = getWorkName(reminderId) 
        workManager.cancelUniqueWork(workName) 
        workManager.cancelAllWorkByTag(workTag) 
        Log.i("ReminderScheduler", "Attempted cancellation for reminder $reminderId (Name: $workName, Tag: $workTag)")
    }

    
    private fun getWorkTag(reminderId: Long) = "reminder_work_tag_$reminderId"
    private fun getWorkName(reminderId: Long) = "reminder_work_name_$reminderId" 

} 