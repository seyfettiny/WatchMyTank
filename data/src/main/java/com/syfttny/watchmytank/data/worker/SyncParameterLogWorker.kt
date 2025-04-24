package com.syfttny.watchmytank.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.util.Date

@HiltWorker
class SyncParameterLogWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val parameterDao: ParameterDao,
    private val firestore: FirebaseFirestore // Inject Firestore
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "SyncParameterLogWorker"
        private const val FIRESTORE_COLLECTION = "parameter_logs" // Define collection name
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting sync work...")

        try {
            val unsyncedLogs = parameterDao.getUnsyncedLogs()
            if (unsyncedLogs.isEmpty()) {
                Log.d(TAG, "No unsynced logs found. Work finished successfully.")
                return Result.success()
            }

            Log.d(TAG, "Found ${unsyncedLogs.size} unsynced logs. Attempting upload...")

            // Consider using Batched Writes for >1 log for efficiency
            // For simplicity here, we upload one by one.
            for (entity in unsyncedLogs) {
                val logMap = entity.toFirestoreMap()
                try {
                    // Upload to Firestore
                    firestore.collection(FIRESTORE_COLLECTION)
                        .add(logMap)
                        .await() // Uses kotlinx-coroutines-play-services

                    // Mark as synced locally ONLY after successful upload
                    parameterDao.markLogAsSynced(entity.id)
                    Log.d(TAG, "Successfully synced log ID: ${entity.id}")

                } catch (e: Exception) {
                    // Handle Firestore upload error
                    Log.e(TAG, "Error syncing log ID: ${entity.id} to Firestore. Error: ${e.message}", e)
                    // Decide on retry logic. Network errors are common reasons to retry.
                    // For this example, we retry on any exception during the loop.
                    // A more robust implementation might check the exception type.
                    return Result.retry()
                }
            }

            Log.d(TAG, "All unsynced logs processed successfully.")
            return Result.success()

        } catch (e: Exception) {
            // Handle errors fetching logs from DAO or other unexpected issues
            Log.e(TAG, "Error during sync work: ${e.message}", e)
            return Result.failure() // Non-retryable failure if DAO fails etc.
        }
    }

    // Helper function to convert Entity to a Map suitable for Firestore
    private fun ParameterLogEntity.toFirestoreMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "parameterType" to this.parameterType.name, // Store enum name as String
            "value" to this.value,
            // Convert LocalDateTime to Firestore Timestamp
            "timestamp" to Timestamp(Date.from(this.timestamp.atZone(ZoneId.systemDefault()).toInstant()))
        )
        this.notes?.let { map["notes"] = it } // Add notes only if not null
        return map
        // Note: We EXCLUDE local 'id' and 'isSynced' fields
    }
} 