package com.syfttny.watchmytank.feature_reminders.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.syfttny.watchmytank.domain.model.Reminder
import javax.inject.Inject
import com.syfttny.watchmytank.core.R as coreR // Import core R class
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Log

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "watchmytank_reminders_channel"
        private const val CHANNEL_NAME = "Aquarium Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for aquarium feeding and maintenance reminders"
    }

    init {
        Log.d("NotificationHelper", "Initializing and creating notification channel.")
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        try {
            //TODO: remove this check because it is always equal and higher than 26
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                    description = CHANNEL_DESCRIPTION
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                Log.i("NotificationHelper", "Notification channel '$CHANNEL_ID' created successfully.")
            }
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Error creating notification channel", e)
        }
    }

    fun showReminderNotification(reminder: Reminder) {
        Log.d("NotificationHelper", "Attempting to show notification for reminder: ${reminder.id} - ${reminder.name}")
        try {
            // TODO: Add PendingIntent to open the app/specific screen when tapped

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(coreR.drawable.ic_launcher_foreground) // Use icon from core
                .setContentTitle("Aquarium Reminder")
                .setContentText("It's time for: ${reminder.name}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Dismiss notification when tapped

            with(NotificationManagerCompat.from(context)) {
                // notificationId is unique for each notification. Using reminder.id ensures
                // we can update or cancel it later if needed. Cast to Int.
                // Be mindful of potential collisions if IDs are very large, though unlikely here.
                val notificationId = reminder.id.toInt()
                Log.i("NotificationHelper", "Notifying with ID: $notificationId, Title: Aquarium Reminder, Text: It's time for: ${reminder.name}")
                notify(notificationId, builder.build())
                Log.i("NotificationHelper", "Notification successfully posted for reminder ID: ${reminder.id}")
            }
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Error posting notification for reminder ID: ${reminder.id}", e)
        }
    }
} 