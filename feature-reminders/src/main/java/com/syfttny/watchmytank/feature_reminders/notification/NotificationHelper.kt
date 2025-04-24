package com.syfttny.watchmytank.feature_reminders.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.syfttny.watchmytank.domain.model.Reminder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.syfttny.watchmytank.core.R as coreR // Import core R class

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "watchmytank_reminders_channel"
        private const val CHANNEL_NAME = "Aquarium Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for aquarium feeding and maintenance reminders"

        // TODO: Finalize this URI scheme and potentially move it to a shared constants location (e.g., :core)
        private const val REMINDER_DEEP_LINK_URI_PATTERN = "app://watchmytank/reminders/%d"
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
            // --- Deep Link Intent Setup --- //
            // TODO: (Notification Routing) Ensure the URI pattern matches the one handled by the :app module's navigation graph and AndroidManifest.xml intent-filter.
            val deepLinkUri = Uri.parse(REMINDER_DEEP_LINK_URI_PATTERN.format(reminder.id))

            val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
                // Important: Limit the intent to your app's package
                `package` = context.packageName
                // Add flags if needed (e.g., to clear task stack or bring existing to front)
                // flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntentFlags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

            // Use reminder.id as the request code for PendingIntent uniqueness per reminder
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                reminder.id.toInt(),
                intent,
                pendingIntentFlags
            )
            // --- End Deep Link Intent Setup --- //

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(coreR.drawable.ic_launcher_foreground) // Use icon from core
                .setContentTitle("Aquarium Reminder") // Consider using reminder.name here?
                .setContentText("It's time for: ${reminder.name}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent to handle tap
                .setAutoCancel(true) // Dismiss notification when tapped

            // notificationId is unique for each notification. Using reminder.id ensures
            // we can update or cancel it later if needed. Cast to Int.
            val notificationId = reminder.id.toInt()

            // Check for POST_NOTIFICATIONS permission before notifying (target S+)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            ) {
                 Log.i("NotificationHelper", "Notifying with ID: $notificationId, Title: Aquarium Reminder, Text: It's time for: ${reminder.name}")
                 notificationManager.notify(notificationId, builder.build())
                 Log.i("NotificationHelper", "Notification successfully posted for reminder ID: ${reminder.id}")
            } else {
                 Log.w("NotificationHelper", "Missing POST_NOTIFICATIONS permission for reminder ID: ${reminder.id}")
                 // TODO: Consider how to handle missing permission (e.g., log, inform user elsewhere?)
            }

        } catch (e: Exception) {
            Log.e("NotificationHelper", "Error posting notification for reminder ID: ${reminder.id}", e)
        }
    }

    fun cancelNotification(reminderId: Long) {
        notificationManager.cancel(reminderId.toInt())
    }
} 