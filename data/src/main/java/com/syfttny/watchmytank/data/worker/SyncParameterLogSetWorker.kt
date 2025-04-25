package com.syfttny.watchmytank.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.mapper.toDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class SyncParameterLogSetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val parameterDao: ParameterDao,
    private val firestore: FirebaseFirestore
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "SyncParameterLogSetWorker"
        // Use a different collection name or structure if desired for sets
        private const val FIRESTORE_COLLECTION = "parameter_log_sets"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting sync work for parameter log sets...")

        try {
            val unsyncedLogSets = parameterDao.getUnsyncedLogSets()
            if (unsyncedLogSets.isEmpty()) {
                Log.d(TAG, "No unsynced log sets found. Work finished successfully.")
                return Result.success()
            }

            Log.d(TAG, "Found ${unsyncedLogSets.size} unsynced log sets. Attempting upload...")

            val collectionRef = firestore.collection(FIRESTORE_COLLECTION)

            // Consider using Batched Writes for efficiency if uploading many sets
            for (entity in unsyncedLogSets) {
                val logSetDto = entity.toDto() // Use the mapper to DTO
                try {
                    // Upload DTO to Firestore. Firestore handles the serialization.
                    collectionRef.add(logSetDto).await()

                    // Mark as synced locally ONLY after successful upload
                    parameterDao.markLogSetAsSynced(entity.id)
                    Log.d(TAG, "Successfully synced log set ID: ${entity.id}")

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Error syncing log set ID: ${entity.id} to Firestore. Error: ${e.message}",
                        e
                    )
                    // Retry on network errors or specific Firestore exceptions
                    // For simplicity, retry on any exception during upload loop
                    return Result.retry()
                }
            }

            Log.d(TAG, "All unsynced log sets processed successfully.")
            return Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching unsynced log sets from DAO: ${e.message}", e)
            // If we can't even read from the DB, it's likely a non-retryable issue
            return Result.failure()
        }
    }
} 